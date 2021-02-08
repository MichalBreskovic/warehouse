package warehouse.storage;

import java.util.List;
import java.util.Stack;

public interface UmiestnenieDao {

	List<Umiestnenie> getAll(); // vypíše všetko vo všetkıch skladoch

	List<Umiestnenie> hladaj(Tovar tovar) throws NullPointerException; // vyh¾adáva pod¾a druhu tovaru vo všetkıch skladoch

	List<Umiestnenie> vypisSklad(Sklad sklad) throws NullPointerException; // vypíše všetko v danom sklade

	boolean jeVolne(int regal, int policka, Sklad sklad) throws NullPointerException; // zisuje, èi je nejaké miesto v sklade obsadené

	Stack<Umiestnenie> volneUmiestnenia(Sklad sklad) throws NullPointerException, StorageIsFullException; //vypíše vo¾né umiestnenia
	
	Umiestnenie getById(Long idUmiestnenia) throws EntityNotFoundException; // vyh¾adávanie pod¾a id umiestnenia

	Umiestnenie naskladnenie(Umiestnenie umiestnenie, Sklad sklad) throws EntityNotFoundException, NullPointerException;

	Umiestnenie vyskladnenie(Long id) throws EntityNotFoundException;
	
}
