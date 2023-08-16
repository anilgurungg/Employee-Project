package com.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeAPIException extends RuntimeException {
	private final HttpStatus status;
	private final String message;
	

	public EmployeeAPIException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	

	public EmployeeAPIException(HttpStatus status, String message, Throwable exception) {
		super(exception);
		this.status = status;
		this.message = message;
	}


	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
