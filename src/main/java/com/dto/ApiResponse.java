package com.dto;
 
 

public class ApiResponse {
	 
	private Boolean success;
	 
	private String message;
	 
	 

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public ApiResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
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
