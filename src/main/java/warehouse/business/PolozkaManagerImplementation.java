package warehouse.business;

import java.util.ArrayList;
import java.util.List;

import warehouse.storage.DaoFactory;
import warehouse.storage.Sklad;
import warehouse.storage.Umiestnenie;
import warehouse.storage.UmiestnenieDao;

public class PolozkaManagerImplementation implements PolozkaManager {

	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();

	@Override
	public List<Polozka> getPolozky(Sklad sklad) {
		List<Umiestnenie> umiestnenia = umiestnenieDao.vypisSklad(sklad);
		List<Polozka> vysledok = new ArrayList<Polozka>();
		boolean uzBolo = false;

		for (Umiestnenie u : umiestnenia) {
			int pocet = u.getMnozstvo();
			double cena = u.getDruh().getJednotkovaCena();

			for (Polozka p : vysledok) {
				if (p.getDruh().equals(u.getDruh())) {
					p.setPocet(p.getPocet() + pocet);
					p.setCelkovaCena(p.getCelkovaCena() + (u.getDruh().getJednotkovaCena() * pocet));
					uzBolo = true;
					break;
				}
			}

			if (uzBolo == true)
				uzBolo = false;
			else
				vysledok.add(new Polozka(u.getDruh(), pocet, cena));

		}
		return vysledok;
	}

}
