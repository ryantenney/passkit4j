package com.ryantenney.passkit4j;

import java.io.IOException;

public class PassDeserializationException extends IOException {

	private static final long serialVersionUID = 2782383453975495855L;

	public PassDeserializationException(String message) {
		super(message);
	}

	public PassDeserializationException(Throwable cause) {
		super(cause);
	}

	public PassDeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
