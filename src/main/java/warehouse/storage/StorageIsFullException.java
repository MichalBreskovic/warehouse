package warehouse.storage;

public class StorageIsFullException extends RuntimeException {

	private static final long serialVersionUID = -7027072627246905504L;

	public StorageIsFullException(String message) {
		super(message);

	}
}
