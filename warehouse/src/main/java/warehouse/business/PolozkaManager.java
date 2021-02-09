package warehouse.business;

import java.util.List;

import warehouse.storage.Sklad;

public interface PolozkaManager {
	List<Polozka> getPolozky(Sklad sklad);
}
