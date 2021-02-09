package warehouse.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MysqlTovarDaoTest {

	private TovarDao tovarDao;
	private Tovar novyTovar;
	private Tovar ulozenyTovar;
	
	private JednotkaDao jednotkaDao;
	private Jednotka novaJednotka;
	private Jednotka ulozenaJednotka;

	MysqlTovarDaoTest() {
		DaoFactory.INSTANCE.testing();
		tovarDao = DaoFactory.INSTANCE.getTovarDao();
		jednotkaDao = DaoFactory.INSTANCE.getJednotkaDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		novaJednotka = new Jednotka("kg");
		ulozenaJednotka = jednotkaDao.save(novaJednotka);
		
		novyTovar = new Tovar("Tovar", ulozenaJednotka, 20, 1, "popis");
		ulozenyTovar = tovarDao.save(novyTovar);
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			tovarDao.delete(ulozenyTovar.getId());
			jednotkaDao.delete(ulozenaJednotka.getId());
		} catch (EntityNotFoundException ignored) {
		}
	}

	@Test
	void testGetAll() {
		List<Tovar> tovary = tovarDao.getAll();
		assertTrue(tovary.size() > 0);
		assertNotNull(tovary.get(0).getNazov());
		assertNotNull(tovary.get(0).getMernaJednotka());
		assertNotNull(tovary.get(0).getJednotkovaCena());
		assertNotNull(tovary.get(0).getObmedzenie());
		assertNotNull(tovary.get(0).getPopis());
	}

	@Test()
	void testGetById() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				tovarDao.getById(-1L);
			}
		});

		Tovar podlaId = tovarDao.getById(ulozenyTovar.getId());
		assertEquals(ulozenyTovar.getId(), podlaId.getId());
		assertEquals(novyTovar.getNazov(), podlaId.getNazov());
		assertEquals(novyTovar.getJednotkovaCena(), podlaId.getJednotkovaCena());
		assertEquals(novyTovar.getMernaJednotka(), podlaId.getMernaJednotka());
		assertEquals(novyTovar.getObmedzenie(), podlaId.getObmedzenie());
		assertEquals(novyTovar.getPopis(), podlaId.getPopis());

	}

	@Test
	void testUpdate() {
		Tovar editovanyTovar = new Tovar(ulozenyTovar.getId(), "AAA", ulozenaJednotka, 20, 3, "bla bla");
		Tovar ulozenaZmena = tovarDao.save(editovanyTovar);
		assertEquals("AAA", ulozenaZmena.getNazov());
		assertEquals(ulozenaJednotka, ulozenaZmena.getMernaJednotka());
		assertEquals(20, ulozenaZmena.getJednotkovaCena());
		assertEquals(3, ulozenaZmena.getObmedzenie());
		assertEquals("bla bla", ulozenaZmena.getPopis());
		assertEquals(editovanyTovar.getId(), ulozenaZmena.getId());

		Tovar tovarInDb = tovarDao.getById(ulozenyTovar.getId());
		assertEquals("AAA", tovarInDb.getNazov());
		assertEquals(ulozenaJednotka, tovarInDb.getMernaJednotka());
		assertEquals(20, tovarInDb.getJednotkovaCena());
		assertEquals(3, tovarInDb.getObmedzenie());
		assertEquals("bla bla", tovarInDb.getPopis());

		editovanyTovar.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				tovarDao.save(editovanyTovar);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				tovarDao.save(null);
			}
		});
	}

	@Test()
	void testDelete() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				tovarDao.delete(-1L);
			}
		});

		Tovar novyTovar2 = new Tovar("Tovar", ulozenaJednotka, 20, 1, "popis");
		Tovar ulozenyTovar2 = tovarDao.save(novyTovar2);
		List<Tovar> originalList = tovarDao.getAll();
		tovarDao.delete(ulozenyTovar2.getId());
		assertEquals(originalList.size() - 1, tovarDao.getAll().size());
	}

}
