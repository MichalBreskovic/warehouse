package warehouse.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {
	
	INSTANCE;

	private boolean testing = false;
	private JdbcTemplate jdbcTemplate;
	private SkladDao skladDao;
	private SkladnikDao skladnikDao;
	private UmiestnenieDao umiestnenieDao;
	private TovarDao tovarDao;
	private JednotkaDao jednotkaDao;

	public void testing() {
		testing = true;
	}

	public SkladDao getSkladDao() {
		if (skladDao == null) {
			skladDao = new MysqlSkladDao(getJdbcTemplate());
		}

		return skladDao;
	}
	
	public UmiestnenieDao getUmiestnenieDao() {
		if (umiestnenieDao == null) {
			umiestnenieDao = new MysqlUmiestnenieDao(getJdbcTemplate());
		}

		return umiestnenieDao;
	}
	
	public SkladnikDao getSkladnikDao() {
		if (skladnikDao == null) {
			skladnikDao = new MysqlSkladnikDao(getJdbcTemplate());
		}

		return skladnikDao;
	}
	
	public TovarDao getTovarDao() {
		if (tovarDao == null) {
			tovarDao = new MysqlTovarDao(getJdbcTemplate());
		}

		return tovarDao;
	}
	
	public JednotkaDao getJednotkaDao() {
		if (jednotkaDao == null) {
			jednotkaDao = new MysqlJednotkaDao(getJdbcTemplate());
		}

		return jednotkaDao;
	}

	private JdbcTemplate getJdbcTemplate() {

		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
//			dataSource.setUser("paz1c");
//			dataSource.setPassword("pazkojesuper");

			dataSource.setUser("michal");
			dataSource.setPassword("12345");
			dataSource.setUrl("jdbc:mysql://localhost/sklady_test?serverTimezone=Europe/Bratislava");
//			if (testing) {
//				dataSource.setUrl("jdbc:mysql://localhost:3306/sklady_test?serverTimezone=Europe/Bratislava");
//			} else {
//				dataSource.setUrl("jdbc:mysql://localhost:3306/sklady?serverTimezone=Europe/Bratislava");
//			}

			jdbcTemplate = new JdbcTemplate(dataSource);
		}

		return jdbcTemplate;

	}
}
