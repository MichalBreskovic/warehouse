package warehouse.storage;

public class EntityNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -422946747680123539L;

	public EntityNotFoundException(String message) {
		super(message);
		
	}

}