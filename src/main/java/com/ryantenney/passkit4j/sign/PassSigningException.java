package com.ryantenney.passkit4j.sign;

public class PassSigningException extends Exception {

	private static final long serialVersionUID = 6021707149897120829L;

	public PassSigningException(String message) {
		super(message);
	}

	public PassSigningException(Throwable cause) {
		super(cause);
	}

	public PassSigningException(String message, Throwable cause) {
		super(message, cause);
	}

}
