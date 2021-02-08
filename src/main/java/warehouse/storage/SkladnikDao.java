package warehouse.storage;

import java.util.List;

public interface SkladnikDao {
	
	List<Skladnik> getAll();

	Skladnik getById(long id) throws EntityNotFoundException;

	Skladnik save(Skladnik skladnik) throws EntityNotFoundException, NullPointerException;

	Skladnik delete(long id) throws EntityNotFoundException;
}
