package warehouse.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {
	
	INSTANCE;

	private boolean testing = true;
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

			// LOCAL
//			dataSource.setUser("michal");
//			dataSource.setPassword("12345");
//			if(testing) dataSource.setUrl("jdbc:mysql://localhost/sklady_test?serverTimezone=Europe/Bratislava");
//			else dataSource.setUrl("jdbc:mysql://localhost/sklady?serverTimezone=Europe/Bratislava");
			
			// CLOUD
			dataSource.setUser("Michal");
			dataSource.setPassword("m9TBqahvjE");
			if(testing) dataSource.setUrl("jdbc:mysql://34.65.232.173:3306/sklady_test");
			else dataSource.setUrl("jdbc:mysql://34.65.232.173:3306/sklady");

			jdbcTemplate = new JdbcTemplate(dataSource);
		}

		return jdbcTemplate;

	}
}
