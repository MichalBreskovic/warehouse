package warehouse.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MysqlUmiestnenieDaoTest {

	private UmiestnenieDao umiestnenieDao;
	private Umiestnenie noveUmiestnenie;
	private Umiestnenie ulozeneUmiestnenie;

	private SkladDao skladDao;
	private Sklad novySklad;
	private Sklad ulozenySklad;

	private TovarDao tovarDao;
	private Tovar novyTovar;
	private Tovar ulozenyTovar;

	private JednotkaDao jednotkaDao;
	private Jednotka novaJednotka;
	private Jednotka ulozenaJednotka;

	MysqlUmiestnenieDaoTest() {
		DaoFactory.INSTANCE.testing();
		umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
		skladDao = DaoFactory.INSTANCE.getSkladDao();
		tovarDao = DaoFactory.INSTANCE.getTovarDao();
		jednotkaDao = DaoFactory.INSTANCE.getJednotkaDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		novySklad = new Sklad("Sklad 1", "SNP 1, 040 01 Košice", 20, 10);
		ulozenySklad = skladDao.save(novySklad);

		novaJednotka = new Jednotka("kg");
		ulozenaJednotka = jednotkaDao.save(novaJednotka);

		novyTovar = new Tovar("Tovar", ulozenaJednotka, 20, 1, "popis");
		ulozenyTovar = tovarDao.save(novyTovar);

		noveUmiestnenie = new Umiestnenie(ulozenyTovar, 10, ulozenySklad, 1, 1);
		ulozeneUmiestnenie = umiestnenieDao.naskladnenie(noveUmiestnenie, ulozenySklad);
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			umiestnenieDao.vyskladnenie(ulozeneUmiestnenie.getId());
			skladDao.delete(ulozenySklad.getId());
			tovarDao.delete(ulozenyTovar.getId());
			jednotkaDao.delete(ulozenaJednotka.getId());
		} catch (EntityNotFoundException ignored) {
		}
	}

	@Test
	void testGetAll() {
		List<Umiestnenie> umiestnenia = umiestnenieDao.getAll();
		assertTrue(umiestnenia.size() > 0);
		assertNotNull(umiestnenia.get(0).getDruh());
		assertNotNull(umiestnenia.get(0).getMnozstvo());
		assertNotNull(umiestnenia.get(0).getSklad());
		assertNotNull(umiestnenia.get(0).getRegal());
		assertNotNull(umiestnenia.get(0).getPolicka());
	}

	@Test()
	void testHladaj() {
		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.hladaj(null);
			}
		});

		List<Umiestnenie> umiestnenia = umiestnenieDao.vypisSklad(ulozenySklad);
		assertTrue(umiestnenia.size() > 0);
		assertNotNull(umiestnenia.get(0).getDruh());
		assertNotNull(umiestnenia.get(0).getMnozstvo());
		assertNotNull(umiestnenia.get(0).getSklad());
		assertNotNull(umiestnenia.get(0).getRegal());
		assertNotNull(umiestnenia.get(0).getPolicka());
	}

	@Test()
	void testVypisSklad() {
		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.vypisSklad(null);
			}
		});

		List<Umiestnenie> umiestnenia = umiestnenieDao.vypisSklad(ulozenySklad);
		assertTrue(umiestnenia.size() > 0);
		assertNotNull(umiestnenia.get(0).getDruh());
		assertNotNull(umiestnenia.get(0).getMnozstvo());
		assertNotNull(umiestnenia.get(0).getSklad());
		assertNotNull(umiestnenia.get(0).getRegal());
		assertNotNull(umiestnenia.get(0).getPolicka());
	}

	@Test()
	void testJeVolne() {
		assertFalse(umiestnenieDao.jeVolne(ulozeneUmiestnenie.getRegal(), ulozeneUmiestnenie.getPolicka(), ulozeneUmiestnenie.getSklad()));
		assertTrue(umiestnenieDao.jeVolne(1, 4, ulozenySklad));
	}

	@Test()
	void testVolneUmiestnenia() {
		Sklad novySklad2 = new Sklad("Sklad 2", "SNP 1, 040 01 Košice", 1, 1);
		Sklad ulozenySklad2 = skladDao.save(novySklad2);

		Umiestnenie noveUmiestnenie2 = new Umiestnenie(ulozenyTovar, 10, ulozenySklad2, 1, 1);
		Umiestnenie ulozeneUmiestnenie2 = umiestnenieDao.naskladnenie(noveUmiestnenie2, ulozenySklad2);

		assertThrows(StorageIsFullException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.volneUmiestnenia(ulozenySklad2);
			}
		});

		umiestnenieDao.vyskladnenie(ulozeneUmiestnenie2.getId());
		skladDao.delete(ulozenySklad2.getId());
	}

	@Test()
	void testGetById() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.getById(-1L);
			}
		});

		Umiestnenie podlaId = umiestnenieDao.getById(ulozeneUmiestnenie.getId());
		assertEquals(ulozeneUmiestnenie.getId(), podlaId.getId());
		assertEquals(noveUmiestnenie.getDruh(), podlaId.getDruh());
		assertEquals(noveUmiestnenie.getMnozstvo(), podlaId.getMnozstvo());
		assertEquals(noveUmiestnenie.getSklad(), podlaId.getSklad());
		assertEquals(noveUmiestnenie.getRegal(), podlaId.getRegal());
		assertEquals(noveUmiestnenie.getPolicka(), podlaId.getPolicka());

	}

	@Test
	void testUpdate() {
		Umiestnenie editovaneUmiestnenie = new Umiestnenie(ulozeneUmiestnenie.getId(), ulozenyTovar, 10, ulozenySklad,
				1, 1);
		Umiestnenie ulozenaZmena = umiestnenieDao.naskladnenie(editovaneUmiestnenie, ulozenySklad);
		assertEquals(ulozenyTovar, ulozenaZmena.getDruh());
		assertEquals(10, ulozenaZmena.getMnozstvo());
		assertEquals(ulozenySklad, ulozenaZmena.getSklad());
		assertEquals(1, ulozenaZmena.getRegal());
		assertEquals(1, ulozenaZmena.getPolicka());
		assertEquals(editovaneUmiestnenie.getId(), ulozenaZmena.getId());

		Umiestnenie umiestnenieInDb = umiestnenieDao.getById(ulozeneUmiestnenie.getId());
		assertEquals(ulozenyTovar, umiestnenieInDb.getDruh());
		assertEquals(10, umiestnenieInDb.getMnozstvo());
		assertEquals(ulozenySklad, umiestnenieInDb.getSklad());
		assertEquals(1, umiestnenieInDb.getRegal());
		assertEquals(1, umiestnenieInDb.getPolicka());

		editovaneUmiestnenie.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.naskladnenie(editovaneUmiestnenie, ulozenySklad);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.naskladnenie(null, ulozenySklad);
			}
		});

		assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.naskladnenie(null, null);
			}
		});
	}

	@Test()
	void testVyskladnenie() {
		assertThrows(EntityNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				umiestnenieDao.vyskladnenie(-1L);
			}
		});

		Umiestnenie noveUmiestnenie2 = new Umiestnenie(ulozenyTovar, 10, ulozenySklad, 1, 3);
		Umiestnenie ulozeneUmiestnenie2 = umiestnenieDao.naskladnenie(noveUmiestnenie2, ulozenySklad);

		List<Umiestnenie> originalList = umiestnenieDao.getAll();
		umiestnenieDao.vyskladnenie(ulozeneUmiestnenie2.getId());
		assertEquals(originalList.size() - 1, umiestnenieDao.getAll().size());

	}

}
