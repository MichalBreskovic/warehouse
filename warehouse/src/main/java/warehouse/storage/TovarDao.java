package warehouse.storage;

import java.util.List;

public interface TovarDao {

	List<Tovar> getAll(); // vyp�e v�etky druhy tovarov

	Tovar getById(long id) throws EntityNotFoundException;

	Tovar save(Tovar save) throws EntityNotFoundException, NullPointerException;

	Tovar delete(long id) throws EntityNotFoundException;

}
