package warehouse.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlUmiestnenieDao implements UmiestnenieDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlUmiestnenieDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Umiestnenie> getAll() {
		return jdbcTemplate.query(
				"SELECT u.id, t.id, t.nazov, t.jednotkova_cena, t.obmedzenie, t.popis, j.id, j.oznacenie, u.mnozstvo, s.id, s.nazov, s.adresa, s.pocet_policiek_v_regali, s.pocet_regalov, u.regal, u.policka FROM umiestnenie u "
						+ " JOIN sklad s ON u.sklad = s.id" + " JOIN tovar t ON u.druh = t.id"
						+ " JOIN jednotka j ON t.merna_jednotka = j.id" + " ORDER BY u.sklad, u.regal, u.policka ASC",
				new UmiestnenieRowMapper());
	}

	public List<Umiestnenie> hladaj(Tovar tovar) throws NullPointerException {
		if (tovar == null) {
			throw new NullPointerException("Druh nesmie byù pr·zdny!");
		} else {

			return jdbcTemplate.query(
					"SELECT u.id, t.id, t.nazov, t.jednotkova_cena, t.obmedzenie, t.popis, j.id, j.oznacenie, u.mnozstvo, s.id, s.nazov, s.adresa, s.pocet_policiek_v_regali, s.pocet_regalov, u.regal, u.policka FROM umiestnenie u "
							+ "	JOIN sklad s ON u.sklad = s.id" + "	JOIN tovar t ON u.druh = t.id"
							+ "	JOIN jednotka j ON t.merna_jednotka = j.id"
							+ "	WHERE u.druh = ? ORDER BY u.regal, u.policka ASC",
					new UmiestnenieRowMapper(), tovar.getId());
		}

	}

	public List<Umiestnenie> vypisSklad(Sklad sklad) throws NullPointerException {
		if (sklad == null) {
			throw new NullPointerException("Sklad nesmie byù pr·zdny!");
		} else {
			return jdbcTemplate.query(
					"SELECT u.id, t.id, t.nazov, t.jednotkova_cena, t.obmedzenie, t.popis, j.id, j.oznacenie, u.mnozstvo, s.id, s.nazov, s.adresa, s.pocet_policiek_v_regali, s.pocet_regalov, u.regal, u.policka FROM umiestnenie u "
							+ "	JOIN sklad s ON u.sklad = s.id" + "	JOIN tovar t ON u.druh = t.id"
							+ "	JOIN jednotka j ON t.merna_jednotka = j.id"
							+ "	WHERE u.sklad = ? ORDER BY u.regal, u.policka ASC",
					new UmiestnenieRowMapper(), sklad.getId());
		}

	}

	public boolean jeVolne(int regal, int policka, Sklad sklad) throws NullPointerException {

		try {
			jdbcTemplate.queryForObject(
					"SELECT u.id, t.id, t.nazov, t.jednotkova_cena, t.obmedzenie, t.popis, j.id, j.oznacenie, u.mnozstvo, s.id, s.nazov, s.adresa, s.pocet_policiek_v_regali, s.pocet_regalov, u.regal, u.policka FROM umiestnenie u "
							+ "	JOIN sklad s ON u.sklad = s.id" + "	JOIN tovar t ON u.druh = t.id"
							+ "	JOIN jednotka j ON t.merna_jednotka = j.id"
							+ "	WHERE u.sklad = ? AND u.regal = ? AND u.policka = ?",
					new UmiestnenieRowMapper(), sklad.getId(), regal, policka);
		} catch (DataAccessException e) {
			return true;
		}

		return false;

	}

	public Stack<Umiestnenie> volneUmiestnenia(Sklad sklad) throws NullPointerException, StorageIsFullException {
		if (sklad == null) {
			throw new NullPointerException("Sklad nesmie byù pr·zdny!");
		} else {
			Long docasneId = 0L;
			boolean uzBolo = false;
			int pocetRegalov = sklad.getPocetRegalov();
			int pocetPoliciekVRegali = sklad.getPocetPoliciek();
			List<Umiestnenie> obsadene = vypisSklad(sklad);
			Stack<Umiestnenie> volne = new Stack<Umiestnenie>();

			for (int i = pocetRegalov; i > 0; i--) {
				for (int j = pocetPoliciekVRegali; j > 0; j--) {
					for (Umiestnenie u : obsadene) {
						if (u.getRegal() == i && u.getPolicka() == j) {
							uzBolo = true;
							docasneId++;
							break;
						}

					}

					if (uzBolo == false) {
						Umiestnenie u = new Umiestnenie(sklad, i, j);
						u.setId(docasneId);
						volne.push(u);
						docasneId++;
					}
					uzBolo = false;

				}
			}
			if (!volne.isEmpty())
				return volne;
			else
				throw new StorageIsFullException("Sklad " + sklad.getNazov() + " (" + sklad.getId() + ") je pln˝!");
		}
	}

	public Umiestnenie naskladnenie(Umiestnenie umiestnenie, Sklad sklad)
			throws EntityNotFoundException, NullPointerException {

		if (umiestnenie.getId() == null) {
			if (jeVolne(umiestnenie.getRegal(), umiestnenie.getPolicka(), sklad)) {

				SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
				insert.withTableName("umiestnenie");
				insert.usingGeneratedKeyColumns("id");
				insert.usingColumns("druh", "mnozstvo", "sklad", "regal", "policka");

				Map<String, Object> valuesMap = new HashMap<>();
				valuesMap.put("druh", umiestnenie.getDruh().getId());
				valuesMap.put("mnozstvo", umiestnenie.getMnozstvo());
				valuesMap.put("sklad", sklad.getId());
				valuesMap.put("regal", umiestnenie.getRegal());
				valuesMap.put("policka", umiestnenie.getPolicka());

				return new Umiestnenie(insert.executeAndReturnKey(valuesMap).longValue(), umiestnenie.getDruh(),
						umiestnenie.getMnozstvo(), sklad, umiestnenie.getRegal(), umiestnenie.getPolicka());
			} else {
				System.out.println("Umiestnenie: [" + sklad.getId() + "," + umiestnenie.getRegal() + ","
						+ umiestnenie.getPolicka() + "] je obsadenÈ!");
				return null;
			}
		} else {
			String sql = "UPDATE umiestnenie SET druh = ?, mnozstvo = ?, sklad = ?, regal = ?, policka = ? WHERE id = ?";
			int changed;
			try {
				changed = jdbcTemplate.update(sql, umiestnenie.getDruh().getId(), umiestnenie.getMnozstvo(),
						umiestnenie.getSklad().getId(), umiestnenie.getRegal(), umiestnenie.getPolicka(),
						umiestnenie.getId());
			} catch (Exception e) {
				throw new EntityNotFoundException("Tovar s id " + umiestnenie.getDruh().getId() + " neexistuje.");
			}
			if (changed == 1) {
				return umiestnenie;
			} else {
				throw new EntityNotFoundException("Umiestnenie s id: " + umiestnenie.getId() + " sa nenaölo.");
			}
		}

	}

	public Umiestnenie getById(Long idUmiestnenia) throws EntityNotFoundException {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT u.id, t.id, t.nazov, t.jednotkova_cena, t.obmedzenie, t.popis, j.id, j.oznacenie, u.mnozstvo, s.id, s.nazov, s.adresa, s.pocet_policiek_v_regali, s.pocet_regalov, u.regal, u.policka FROM umiestnenie u "
							+ "	JOIN sklad s ON u.sklad = s.id" + "	JOIN tovar t ON u.druh = t.id"
							+ "	JOIN jednotka j ON t.merna_jednotka = j.id" + "	WHERE u.id = ?",
					new UmiestnenieRowMapper(), idUmiestnenia);
		} catch (DataAccessException e) {
			throw new EntityNotFoundException("Umiestnenie s id: " + idUmiestnenia + " sa nenaölo.");
		}
	}

	public Umiestnenie vyskladnenie(Long id) throws EntityNotFoundException {
		try {
			Umiestnenie umiestnenie = getById(id);
			String sql = "DELETE FROM umiestnenie WHERE id =" + id;
			jdbcTemplate.update(sql);
			return umiestnenie;
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Umiestnenie s id: " + id + " sa nenaölo.");
		}
	}

	private class UmiestnenieRowMapper implements RowMapper<Umiestnenie> {

		public Umiestnenie mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long idUmiestnenie = rs.getLong("u.id");
			Long idTovar = rs.getLong("t.id");
			String nazovTovar = rs.getString("t.nazov");

			int idJednotka = rs.getInt("j.id");
			String oznacenieJednotka = rs.getString("j.oznacenie");

			double jednotkovaCenaTovar = rs.getDouble("t.jednotkova_cena");
			int obmedzenieTovar = rs.getInt("t.obmedzenie");
			String popisTovar = rs.getString("t.popis");

			long idSklad = rs.getLong("s.id");
			String nazovSklad = rs.getString("s.nazov");
			String adresaSklad = rs.getString("s.adresa");
			int pocetRegalovSklad = rs.getInt("s.pocet_regalov");
			int pocetPoliciekSklad = rs.getInt("s.pocet_policiek_v_regali");

			int mnozstvoUmiestnenie = rs.getInt("u.mnozstvo");
			int regalUmiestnenie = rs.getInt("u.regal");
			int polickaUmiestnenie = rs.getInt("u.policka");

			Sklad sklad = new Sklad(idSklad, nazovSklad, adresaSklad, pocetRegalovSklad, pocetPoliciekSklad);
			Jednotka jednotka = new Jednotka(idJednotka, oznacenieJednotka);
			Tovar tovar = new Tovar(idTovar, nazovTovar, jednotka, jednotkovaCenaTovar, obmedzenieTovar, popisTovar);

			return new Umiestnenie(idUmiestnenie, tovar, mnozstvoUmiestnenie, sklad, regalUmiestnenie,
					polickaUmiestnenie);

		}

	}
}
