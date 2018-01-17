package com.zxd.exception;

public class InvalidDataException extends RuntimeException {
	private static final long serialVersionUID = 4532060676548861956L;

	public InvalidDataException(String message) {
		super(message);
	}

}
