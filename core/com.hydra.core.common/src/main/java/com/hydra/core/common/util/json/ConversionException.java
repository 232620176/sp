package com.hydra.core.common.util.json;
public class ConversionException extends RuntimeException {

	private static final long serialVersionUID = 3737829749859754613L;

	public ConversionException() {
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
