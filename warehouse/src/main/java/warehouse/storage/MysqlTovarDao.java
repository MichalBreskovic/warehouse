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

public class MysqlTovarDao implements TovarDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlTovarDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Tovar> getAll() {
		return jdbcTemplate.query("SELECT tovar.id, nazov, jednotkova_cena, obmedzenie, popis, jednotka.id, oznacenie FROM tovar "
				+ "JOIN jednotka ON tovar.merna_jednotka = jednotka.id ORDER BY tovar.id ASC",
				new TovarRowMapper());
	}

	@Override
	public Tovar getById(long id) throws EntityNotFoundException {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT tovar.id, nazov, jednotkova_cena, obmedzenie, popis, jednotka.id, oznacenie FROM tovar "
					+ "JOIN jednotka ON tovar.merna_jednotka = jednotka.id WHERE tovar.id = ?",
					new TovarRowMapper(), id);
		} catch (DataAccessException e) {
			throw new EntityNotFoundException("Tovar s id: " + id + " sa nenašiel.");
		}
	}

	@Override
	public Tovar save(Tovar tovar) throws EntityNotFoundException, NullPointerException {
		if (tovar.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("tovar");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("nazov", "merna_jednotka", "jednotkova_cena", "obmedzenie", "popis");

			Map<String, Object> valuesMap = new HashMap<>();
			valuesMap.put("nazov", tovar.getNazov());
			valuesMap.put("merna_jednotka", tovar.getMernaJednotka().getId());
			valuesMap.put("jednotkova_cena", tovar.getJednotkovaCena());
			valuesMap.put("obmedzenie", tovar.getObmedzenie());
			valuesMap.put("popis", tovar.getPopis());

			return new Tovar(insert.executeAndReturnKey(valuesMap).longValue(), tovar.getNazov(),
					tovar.getMernaJednotka(), tovar.getJednotkovaCena(), tovar.getObmedzenie(), tovar.getPopis());

		} else {

			String sql = "UPDATE tovar SET nazov = ?, merna_jednotka = ?, jednotkova_cena = ?, obmedzenie = ?, popis = ? WHERE id = ?";
			int changed = jdbcTemplate.update(sql, tovar.getNazov(), tovar.getMernaJednotka().getId(),
					tovar.getJednotkovaCena(), tovar.getObmedzenie(), tovar.getPopis(), tovar.getId());
			if (changed == 1) {
				return tovar;
			} else {
				throw new EntityNotFoundException("Druh tovaru s id" + tovar.getId() + " sa nenašiel.");
			}
		}
	}

	@Override
	public Tovar delete(long id) throws EntityNotFoundException {
		Tovar tovar = getById(id);
		String sql = "DELETE FROM tovar WHERE id =" + id;
		jdbcTemplate.update(sql);
		return tovar;
	}

	private class TovarRowMapper implements RowMapper<Tovar> {

		public Tovar mapRow(ResultSet rs, int rowNum) throws SQLException {
			long tovarId = rs.getLong("tovar.id");
			String nazov = rs.getString("nazov");
			int jednotkaId = rs.getInt("jednotka.id");
			String oznacenie = rs.getString("oznacenie");
			double jednotkovaCena = rs.getDouble("jednotkova_cena");
			int obmedzenie = rs.getInt("obmedzenie");
			String popis = rs.getString("popis");
			return new Tovar(tovarId, nazov, new Jednotka(jednotkaId, oznacenie), jednotkovaCena, obmedzenie, popis);
		}

	}

}
