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

public class MysqlJednotkaDao implements JednotkaDao {

	private JdbcTemplate jdbcTemplate;

	public MysqlJednotkaDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Jednotka> getAll() {
		return jdbcTemplate.query("SELECT id, oznacenie FROM jednotka", new JednotkaRowMapper());
	}

	@Override
	public Jednotka getById(int id) {
		try {
			return jdbcTemplate.queryForObject("SELECT id, oznacenie FROM jednotka WHERE id = ?",
					new JednotkaRowMapper(), id);
		} catch (DataAccessException e) {
			throw new EntityNotFoundException("Jednotka s id: " + id + " sa nenašla.");
		}
	}
	
	@Override
	public Jednotka save(Jednotka jednotka) throws EntityNotFoundException, NullPointerException {
		if (jednotka.getId() == 0) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("jednotka");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("oznacenie");

			Map<String, String> valuesMap = new HashMap<>();
			valuesMap.put("oznacenie", jednotka.getOznacenie());
	
			return new Jednotka(insert.executeAndReturnKey(valuesMap).intValue(), jednotka.getOznacenie());

		} else {

			String sql = "UPDATE jednotka SET oznacenie = ? WHERE id = ?";
			int changed = jdbcTemplate.update(sql, jednotka.getOznacenie(), jednotka.getId());
			if (changed == 1) {
				return jednotka;
			} else {
				throw new EntityNotFoundException("Jednotka s id" + jednotka.getId() + " sa nenašla.");
			}
		}
	}

	@Override
	public Jednotka delete(int id) throws EntityNotFoundException {
		Jednotka jednotka = getById(id);
		String sql = "DELETE FROM jednotka WHERE id =" + id;
		jdbcTemplate.update(sql);
		return jednotka;
	}

	private class JednotkaRowMapper implements RowMapper<Jednotka> {

		public Jednotka mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String oznacenie = rs.getString("oznacenie");
			return new Jednotka(id, oznacenie);
		}

	}

}
