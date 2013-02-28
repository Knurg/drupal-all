package nerimd.annotation;

public class BadPropertyException extends RuntimeException {
	public static final long serialVersionUID = 74839243824789l;
	
	public BadPropertyException() {
		super();
	}

	public BadPropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadPropertyException(String message) {
		super(message);
	}

	public BadPropertyException(Throwable cause) {
		super(cause);
	}

	
}
