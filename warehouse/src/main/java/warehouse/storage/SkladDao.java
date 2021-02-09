package warehouse.storage;

import java.util.List;

public interface SkladDao {

	List<Sklad> getAll();
	
	Sklad getById(long id) throws EntityNotFoundException;

	Sklad save(Sklad sklad) throws EntityNotFoundException, NullPointerException;

	Sklad delete(long id) throws EntityNotFoundException;

}
