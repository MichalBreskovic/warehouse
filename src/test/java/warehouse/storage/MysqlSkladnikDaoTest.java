package warehouse.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MysqlSkladnikDaoTest {

	private SkladDao skladDao;
	private Sklad novySklad;
	private Sklad ulozenySklad;

	private SkladnikDao skladnikDao;
	private Skladnik novySkladnik;
	private Skladnik ulozenySkladnik;

	MysqlSkladnikDaoTest() {
		DaoFactory.INSTANCE.testing();
		skladDao = DaoFactory.INSTANCE.getSkladDao();
		skladnikDao = DaoFactory.INSTANCE.getSkladnikDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		novySklad = new Sklad("Sklad 1", "SNP 1, 040 01 Košice", 20, 10);
		ulozenySklad = skladDao.save(novySklad);

		novySkladnik = new Skladnik("Jožko", "Mrkvièka", ulozenySklad, true);
		ulozenySkladnik = skladnikDao.save(novySkladnik);
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			skladnikDao.delete(ulozenySkladnik.getId());
			skladDao.delete(ulozenySklad.getId());
		} catch (EntityNotFoundException ignored) {
		}
	}

	@Test
	void testGetAll() {
		List<Skladnik> skladnici = skladnikDao.getAll();
		assertTrue(skladnici.size() > 0);
		assertNotNull(skladnici.get(0).getMeno());
		assertNotNull(skladnici.get(0).getPriezvisko());
		assertNotNull(skladnici.get(0).getSklad());
		assertNotNull(skladnici.get(0).isVeduci());

	}

	@Test()
	void testGetById() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladnikDao.getById(-1L);
			}
		});

		Skladnik podlaId = skladnikDao.getById(ulozenySkladnik.getId());
		assertEquals(ulozenySkladnik.getId(), podlaId.getId());
		assertEquals(ulozenySkladnik.getMeno(), podlaId.getMeno());
		assertEquals(ulozenySkladnik.getPriezvisko(), podlaId.getPriezvisko());
		assertEquals(ulozenySkladnik.getSklad(), podlaId.getSklad());
		assertEquals(ulozenySkladnik.isVeduci(), podlaId.isVeduci());

	}

	@Test
	void testUpdate() {
		Skladnik editovanySkladnik = new Skladnik(ulozenySkladnik.getId(), "AAA", "BBB", ulozenySklad, false);
		Skladnik ulozenaZmena = skladnikDao.save(editovanySkladnik);
		assertEquals(editovanySkladnik.getId(), ulozenaZmena.getId());
		assertEquals(editovanySkladnik.getMeno(), ulozenaZmena.getMeno());
		assertEquals(editovanySkladnik.getPriezvisko(), ulozenaZmena.getPriezvisko());
		assertEquals(editovanySkladnik.getSklad(), ulozenaZmena.getSklad());
		assertEquals(editovanySkladnik.isVeduci(), ulozenaZmena.isVeduci());

		Skladnik skladnikInDb = skladnikDao.getById(ulozenySkladnik.getId());
		assertEquals("AAA", skladnikInDb.getMeno());
		assertEquals("BBB", skladnikInDb.getPriezvisko());
		assertEquals(ulozenySklad, skladnikInDb.getSklad());
		assertEquals(false, skladnikInDb.isVeduci());

		editovanySkladnik.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladnikDao.save(editovanySkladnik);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladnikDao.save(null);
			}
		});
	}

	@Test()
	void testDelete() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				skladnikDao.delete(-1L);
			}
		});
		
		Skladnik novySkladnik2 = new Skladnik("Jožko", "Mrkvièka", ulozenySklad, true);
		Skladnik ulozenySkladnik2 = skladnikDao.save(novySkladnik2);
		List<Skladnik> originalList = skladnikDao.getAll();
		skladnikDao.delete(ulozenySkladnik2.getId());
		assertEquals(originalList.size() - 1, skladnikDao.getAll().size());
	}

}
