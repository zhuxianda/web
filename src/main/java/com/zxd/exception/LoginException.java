package com.zxd.exception;

//反操作
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 4532060676548861956L;
	
	public LoginException(String message) {
		super(message);
	}

}
