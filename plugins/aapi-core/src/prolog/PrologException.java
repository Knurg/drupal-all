package prolog;


public class PrologException extends RuntimeException {
	public static final long serialVersionUID = 343343434;

	public PrologException() {
		super();
	}

	public PrologException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrologException(String message) {
		super(message);
	}

	public PrologException(Throwable cause) {
		super(cause);
	}
}
