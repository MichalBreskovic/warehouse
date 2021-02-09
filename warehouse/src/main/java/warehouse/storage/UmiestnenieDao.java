package warehouse.storage;

import java.util.List;
import java.util.Stack;

public interface UmiestnenieDao {

	List<Umiestnenie> getAll(); // vyp�e v�etko vo v�etk�ch skladoch

	List<Umiestnenie> hladaj(Tovar tovar) throws NullPointerException; // vyh�ad�va pod�a druhu tovaru vo v�etk�ch skladoch

	List<Umiestnenie> vypisSklad(Sklad sklad) throws NullPointerException; // vyp�e v�etko v danom sklade

	boolean jeVolne(int regal, int policka, Sklad sklad) throws NullPointerException; // zis�uje, �i je nejak� miesto v sklade obsaden�

	Stack<Umiestnenie> volneUmiestnenia(Sklad sklad) throws NullPointerException, StorageIsFullException; //vyp�e vo�n� umiestnenia
	
	Umiestnenie getById(Long idUmiestnenia) throws EntityNotFoundException; // vyh�ad�vanie pod�a id umiestnenia

	Umiestnenie naskladnenie(Umiestnenie umiestnenie, Sklad sklad) throws EntityNotFoundException, NullPointerException;

	Umiestnenie vyskladnenie(Long id) throws EntityNotFoundException;
	
}
