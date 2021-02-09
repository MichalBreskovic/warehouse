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

public class MysqlSkladDao implements SkladDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlSkladDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Sklad> getAll() {
		return jdbcTemplate.query("SELECT id, nazov, adresa, pocet_regalov, pocet_policiek_v_regali FROM sklad",
				new SkladRowMapper());
	}

	public Sklad getById(long id) throws EntityNotFoundException {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT id, nazov, adresa, pocet_regalov, pocet_policiek_v_regali FROM sklad WHERE id = ?",
					new SkladRowMapper(), id);
		} catch (DataAccessException e) {
			throw new EntityNotFoundException("Sklad s id: " + id + " sa nenašiel.");
		}
	}

	public Sklad save(Sklad sklad) throws EntityNotFoundException, NullPointerException {
		if (sklad.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("sklad");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("nazov", "adresa", "pocet_regalov", "pocet_policiek_v_regali");

			Map<String, Object> valuesMap = new HashMap<>();
			valuesMap.put("nazov", sklad.getNazov());
			valuesMap.put("adresa", sklad.getAdresa());
			valuesMap.put("pocet_regalov", sklad.getPocetRegalov());
			valuesMap.put("pocet_policiek_v_regali", sklad.getPocetPoliciek());

			return new Sklad(insert.executeAndReturnKey(valuesMap).longValue(), sklad.getNazov(), sklad.getAdresa(),
					sklad.getPocetRegalov(), sklad.getPocetPoliciek());

		} else {

			String sql = "UPDATE sklad SET nazov = ?, adresa = ?, pocet_regalov = ?, pocet_policiek_v_regali = ? WHERE id = ?";
			int changed = jdbcTemplate.update(sql, sklad.getNazov(), sklad.getAdresa(), sklad.getPocetRegalov(),
					sklad.getPocetPoliciek(), sklad.getId());
			if (changed == 1) {
				return sklad;
			} else {
				throw new EntityNotFoundException("Sklad s id" + sklad.getId() + " sa nenašla.");
			}
		}
	}

	public Sklad delete(long id) throws EntityNotFoundException {
		try {
			Sklad sklad = getById(id);
			String sql = "DELETE FROM sklad WHERE id =" + id;
			jdbcTemplate.update(sql);
			return sklad;
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Sklad s id: " + id + " sa nenašiel.");
		}
	}

	private class SkladRowMapper implements RowMapper<Sklad> {

		public Sklad mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			String nazov = rs.getString("nazov");
			String adresa = rs.getString("adresa");
			int pocetRegalov = rs.getInt("pocet_regalov");
			int pocetPoliciek = rs.getInt("pocet_policiek_v_regali");
			return new Sklad(id, nazov, adresa, pocetRegalov, pocetPoliciek);

		}

	}
}