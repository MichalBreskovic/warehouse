package warehouse.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlSkladnikDao implements SkladnikDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlSkladnikDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Skladnik> getAll() {
		return jdbcTemplate.query("SELECT skladnik.id, meno, priezvisko, sklad, veduci, sklad.id, sklad.nazov, sklad.adresa, sklad.pocet_regalov, sklad.pocet_policiek_v_regali FROM skladnik "
				+ "JOIN sklad ON skladnik.sklad = sklad.id", new SkladnikRowMapper());
	}

	public Skladnik getById(long id) throws EntityNotFoundException {
		try {
			return jdbcTemplate.queryForObject("SELECT skladnik.id, meno, priezvisko, sklad, veduci, sklad.id, sklad.nazov, sklad.adresa, sklad.pocet_regalov, sklad.pocet_policiek_v_regali FROM skladnik "
					+ "JOIN sklad ON skladnik.sklad = sklad.id WHERE skladnik.id = ?",
					new SkladnikRowMapper(), id);
		} catch (DataAccessException e) {
			throw new EntityNotFoundException("Skladnik s id: " + id + " sa nenašiel.");
		}
	}

	public Skladnik save(Skladnik skladnik) throws EntityNotFoundException, NullPointerException {
		if (skladnik.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("skladnik");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("meno", "priezvisko", "sklad", "veduci");

			Map<String, Object> valuesMap = new HashMap<>();
			valuesMap.put("meno", skladnik.getMeno());
			valuesMap.put("priezvisko", skladnik.getPriezvisko());
			valuesMap.put("sklad", skladnik.getSklad().getId());
			valuesMap.put("veduci", skladnik.isVeduci());

			return new Skladnik(insert.executeAndReturnKey(valuesMap).longValue(), skladnik.getMeno(),
					skladnik.getPriezvisko(), skladnik.getSklad(), skladnik.isVeduci());

		} else {

			String sql = "UPDATE skladnik SET meno = ?, priezvisko = ?, sklad = ?, veduci = ? WHERE id = ?";
			int changed = jdbcTemplate.update(sql, skladnik.getMeno(), skladnik.getPriezvisko(), skladnik.getSklad().getId(),
					skladnik.isVeduci(), skladnik.getId());
			if (changed == 1) {
				return skladnik;
			} else {
				throw new EntityNotFoundException("Skladnik s id" + skladnik.getId() + " sa nenašla.");
			}
		}

	}

	public Skladnik delete(long id) throws EntityNotFoundException {
		try {
			Skladnik skladnik = getById(id);
			String sql = "DELETE FROM skladnik WHERE id =" + id;
			jdbcTemplate.update(sql);
			return skladnik;
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Skladnik s id: " + id + " sa nenašiel.");
		}

	}

	private class SkladnikRowMapper implements RowMapper<Skladnik> {

		public Skladnik mapRow(ResultSet rs, int rowNum) throws SQLException {
			long idSkladnik = rs.getLong("skladnik.id");
			String menoSkladnik = rs.getString("skladnik.meno");
			String priezviskoSkladnik = rs.getString("skladnik.priezvisko");
			boolean veduciSkladnik = rs.getBoolean("skladnik.veduci");
			
			long idSklad = rs.getLong("sklad.id");
			String nazovSklad = rs.getString("sklad.nazov");
			String adresaSklad = rs.getString("sklad.adresa");
			int pocetRegalovSklad = rs.getInt("sklad.pocet_regalov");
			int pocetPoliciekSklad = rs.getInt("sklad.pocet_policiek_v_regali");
			
			Sklad sklad = new Sklad(idSklad, nazovSklad, adresaSklad, pocetRegalovSklad, pocetPoliciekSklad);
			
			return new Skladnik(idSkladnik, menoSkladnik, priezviskoSkladnik, sklad, veduciSkladnik);

		}

	}
}
