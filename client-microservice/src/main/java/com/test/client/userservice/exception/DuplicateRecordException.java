package com.test.client.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateRecordException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DuplicateRecordException(String message) {
		super(message);
	}
	
	public DuplicateRecordException(String message, Throwable t) {
		super(message, t);
	}
}
