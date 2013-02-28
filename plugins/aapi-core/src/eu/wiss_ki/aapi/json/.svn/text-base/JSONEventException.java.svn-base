package eu.wiss_ki.aapi.json;

import org.json.JSONObject;

public class JSONEventException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5538900555475743772L;
	
	JSONObject o = null;
	
	JSONEventException() {
		super();
		// TODO Auto-generated constructor stub
	}

	JSONEventException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	JSONEventException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	JSONEventException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	JSONEventException(String message, Throwable cause, JSONObject o) {
		super(message, cause);
		this.o = o;
	}

	JSONEventException(String message, JSONObject o) {
		super(message);
		this.o = o;
	}

	JSONEventException(Throwable cause, JSONObject o) {
		super(cause);
		this.o = o;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + ": " + o.toString();
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage() + ": " + o.toString();
	}
	
}
