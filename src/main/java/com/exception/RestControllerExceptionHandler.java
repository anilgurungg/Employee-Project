package com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.dto.ApiResponse;

@ControllerAdvice
public class RestControllerExceptionHandler {
	
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiResponse> resolveException(AccessDeniedException exception) {

	ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,exception.getMessage());

		return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public ResponseEntity<ApiResponse> resolveException(ResourceNotFoundException exception, WebRequest request) {
//		ApiResponse apiResponse = new ApiResponse(false, "Status not found" );
		return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.buildApiResponse());
	}
	
	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ResponseEntity<String> resolveException(BadRequestException exception ) {
 
		return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
	}
	
	@ExceptionHandler(EmployeeAPIException.class)
	@ResponseBody
	public ResponseEntity<String> resolveException(EmployeeAPIException exception ) {
 
		return   ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
	}

	 @ExceptionHandler(Exception.class)
	    @ResponseBody
	    public ResponseEntity<String> handleOtherExceptions(Exception ex, WebRequest request) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
	    }
	 @ExceptionHandler(UnAuthorizedException.class)
		@ResponseBody
		@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
		public ResponseEntity<ApiResponse> resolveException(UnAuthorizedException exception) {

			ApiResponse apiResponse = exception.getApiResponse();

			return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
		}
	 
	 
}
