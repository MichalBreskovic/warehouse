package warehouse.storage;

import java.util.List;

public interface JednotkaDao {
	
	List<Jednotka> getAll(); // vypíše všetky jednotky
	
	Jednotka getById(int id) throws EntityNotFoundException;

	Jednotka save(Jednotka save) throws EntityNotFoundException, NullPointerException;

	Jednotka delete(int id) throws EntityNotFoundException;
}
