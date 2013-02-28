package nerimd.annotation;

public class MissingAttributeException extends RuntimeException {
	public static final long serialVersionUID = 10935480000l;

	public MissingAttributeException() {
		super();
	}

	public MissingAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingAttributeException(String message) {
		super(message);
	}

	public MissingAttributeException(Throwable cause) {
		super(cause);
	}
	
	
}
