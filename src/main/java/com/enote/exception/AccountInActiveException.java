package com.enote.exception;

public class AccountInActiveException extends RuntimeException {
	
	public AccountInActiveException(String message) {
		super(message);
	}
}
