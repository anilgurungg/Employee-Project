package com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dto.ApiResponse;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	 private final String resourceName;
	    private final String fieldName;
	    private final Object fieldValue;

	    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
	        super(buildMessage(resourceName, fieldName, fieldValue));
	        this.resourceName = resourceName;
	        this.fieldName = fieldName;
	        this.fieldValue = fieldValue;
	    }

	    public String getResourceName() {
	        return resourceName;
	    }

	    public String getFieldName() {
	        return fieldName;
	    }

	    public Object getFieldValue() {
	        return fieldValue;
	    }

	    // Helper method to build the error message
	    private static String buildMessage(String resourceName, String fieldName, Object fieldValue) {
	        return String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);
	    }
	    
	    public ApiResponse buildApiResponse() {
	        ApiResponse response = new ApiResponse();
	        response.setSuccess(false);
	        response.setMessage(getMessage());
	        // You can customize the timestamp or other fields as needed
	        return response;
	    }
	
}
