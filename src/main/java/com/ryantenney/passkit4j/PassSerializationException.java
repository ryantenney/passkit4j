package com.ryantenney.passkit4j;

import java.io.IOException;

public class PassSerializationException extends IOException {

	private static final long serialVersionUID = 2782383453975495855L;

	public PassSerializationException(String message) {
		super(message);
	}

	public PassSerializationException(Throwable cause) {
		super(cause);
	}

	public PassSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
