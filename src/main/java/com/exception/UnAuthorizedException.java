package com.exception;

import com.dto.ApiResponse;

public class UnAuthorizedException extends RuntimeException {
	private ApiResponse apiResponse;

	private String message;

	public UnAuthorizedException(ApiResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public UnAuthorizedException(String message) {
		super(message);
		this.message = message;
	}

	public UnAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(ApiResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
