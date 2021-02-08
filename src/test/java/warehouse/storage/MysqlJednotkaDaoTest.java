package warehouse.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MysqlJednotkaDaoTest {

	private JednotkaDao jednotkaDao;
	private Jednotka novaJednotka;
	private Jednotka ulozenaJednotka;

	MysqlJednotkaDaoTest() {
		DaoFactory.INSTANCE.testing();
		jednotkaDao = DaoFactory.INSTANCE.getJednotkaDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		novaJednotka = new Jednotka("kg");
		ulozenaJednotka = jednotkaDao.save(novaJednotka);
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			jednotkaDao.delete(ulozenaJednotka.getId());
		} catch (EntityNotFoundException ignored) {
		}
	}

	@Test
	void testGetAll() {
		List<Jednotka> jednotky = jednotkaDao.getAll();
		assertTrue(jednotky.size() > 0);
		assertNotNull(jednotky.get(0).getOznacenie());

	}

	@Test()
	void testGetById() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				jednotkaDao.getById(-1);
			}
		});

		Jednotka podlaId = jednotkaDao.getById(ulozenaJednotka.getId());
		assertEquals(ulozenaJednotka.getId(), podlaId.getId());
		assertEquals(ulozenaJednotka.getOznacenie(), podlaId.getOznacenie());

	}

	@Test
	void testUpdate() {
		Jednotka editovanaJednotka = new Jednotka(ulozenaJednotka.getId(), "AAA");
		Jednotka ulozenaZmena = jednotkaDao.save(editovanaJednotka);
		assertEquals(editovanaJednotka.getId(), ulozenaZmena.getId());

		Jednotka jednotkaInDb = jednotkaDao.getById(ulozenaJednotka.getId());
		assertEquals("AAA", jednotkaInDb.getOznacenie());

		editovanaJednotka.setId(-1);
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				jednotkaDao.save(editovanaJednotka);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				jednotkaDao.save(null);
			}
		});
	}

	@Test()
	void testDelete() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				jednotkaDao.delete(-1);
			}
		});

		Jednotka novaJednotka2 = new Jednotka("kg");
		Jednotka ulozenaJednotka2 = jednotkaDao.save(novaJednotka2);
		List<Jednotka> originalList = jednotkaDao.getAll();
		jednotkaDao.delete(ulozenaJednotka2.getId());
		assertEquals(originalList.size() - 1, jednotkaDao.getAll().size());
	}

}
