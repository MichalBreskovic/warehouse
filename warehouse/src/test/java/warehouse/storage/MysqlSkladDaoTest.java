package warehouse.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MysqlSkladDaoTest {

	private SkladDao skladDao;
	private Sklad novySklad;
	private Sklad ulozenySklad;

	MysqlSkladDaoTest() {
		DaoFactory.INSTANCE.testing();
		skladDao = DaoFactory.INSTANCE.getSkladDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		novySklad = new Sklad("Sklad 1", "SNP 1, 040 01 Košice", 20, 10);
		ulozenySklad = skladDao.save(novySklad);
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			skladDao.delete(ulozenySklad.getId());
		} catch (EntityNotFoundException ignored) {
		}
	}

	@Test
	void testGetAll() {
		List<Sklad> sklady = skladDao.getAll();
		assertTrue(sklady.size() > 0);
		assertNotNull(sklady.get(0).getNazov());
		assertNotNull(sklady.get(0).getAdresa());
		assertNotNull(sklady.get(0).getPocetRegalov());
		assertNotNull(sklady.get(0).getPocetPoliciek());

	}

	@Test()
	void testGetById() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladDao.getById(-1L);
			}
		});

		Sklad podlaId = skladDao.getById(ulozenySklad.getId());
		assertEquals(ulozenySklad.getId(), podlaId.getId());
		assertEquals(ulozenySklad.getNazov(), podlaId.getNazov());
		assertEquals(ulozenySklad.getAdresa(), podlaId.getAdresa());
		assertEquals(ulozenySklad.getPocetRegalov(), podlaId.getPocetRegalov());
		assertEquals(ulozenySklad.getPocetPoliciek(), podlaId.getPocetPoliciek());

	}

	@Test
	void testUpdate() {
		Sklad editovanySklad = new Sklad(ulozenySklad.getId(), "AAA", "BBB", 2, 10);
		Sklad ulozenaZmena = skladDao.save(editovanySklad);
		assertEquals(editovanySklad.getId(), ulozenaZmena.getId());
		assertEquals(editovanySklad.getNazov(), ulozenaZmena.getNazov());
		assertEquals(editovanySklad.getAdresa(), ulozenaZmena.getAdresa());
		assertEquals(editovanySklad.getPocetRegalov(), ulozenaZmena.getPocetRegalov());
		assertEquals(editovanySklad.getPocetPoliciek(), ulozenaZmena.getPocetPoliciek());

		Sklad skladInDb = skladDao.getById(ulozenySklad.getId());
		assertEquals("AAA", skladInDb.getNazov());
		assertEquals("BBB", skladInDb.getAdresa());
		assertEquals(2, skladInDb.getPocetRegalov());
		assertEquals(10, skladInDb.getPocetPoliciek());

		editovanySklad.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladDao.save(editovanySklad);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladDao.save(null);
			}
		});
	}

	@Test()
	void testDelete() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladDao.delete(-1L);
			}
		});

		Sklad novySklad2 = new Sklad("Sklad 1", "SNP 1, 040 01 Košice", 20, 10);
		Sklad ulozenySklad2 = skladDao.save(novySklad2);
		List<Sklad> originalList = skladDao.getAll();
		skladDao.delete(ulozenySklad2.getId());
		assertEquals(originalList.size() - 1, skladDao.getAll().size());
	}

}
