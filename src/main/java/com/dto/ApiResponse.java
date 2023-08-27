package com.dto;

import java.util.List;

public class ApiResponse {
	 
	private Boolean success;
	 
	private String message;
	 
	private List<String> errorStrings;
	 

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public ApiResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public ApiResponse(Boolean success, String message, List<String> errorStrings) {
		super();
		this.success = success;
		this.message = message;
		this.errorStrings = errorStrings;
	}

	
 

	public List<String> getErrorStrings() {
		return errorStrings;
	}



	public void setErrorStrings(List<String> errorStrings) {
		this.errorStrings = errorStrings;
	}



	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	@Override
	public String toString() {
		return "ApiResponse [success=" + success + ", message=" + message + "]";
	}

	
 
}
