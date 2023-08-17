package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.PagedResponseDTO;
import com.exception.BadRequestException;
import com.service.EmployeeService;
import com.utils.AppConstants;

import javassist.expr.NewArray;

@RequestMapping("/api/v2")
@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	 
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/employees")
	public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO ) {
		EmployeeDTO savedEmployeeDTO = employeeService.registerEmp(employeeDTO);
		return new ResponseEntity<>( savedEmployeeDTO , HttpStatus.CREATED);
	}
	
	
	@GetMapping("/employees")
	public ResponseEntity<PagedResponseDTO<EmployeeDTO>> getAllEmployees(@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponseDTO<EmployeeDTO> listOfEmployees = employeeService.getAllEmployees(page, size);
		return new ResponseEntity<>(listOfEmployees, HttpStatus.OK); 
	 
	}
	
	@GetMapping("/employees/search")
	public ResponseEntity<List<EmployeeDTO>> searchEmployees(
			@RequestParam(required = false, name = "searchTerm") String searchTerm			
			) {
		List<EmployeeDTO> listOfEmployees = employeeService.searchEmployees(searchTerm);
		return new ResponseEntity<>(listOfEmployees, HttpStatus.OK); 
	 
	}
	
	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<EmployeeDTO> getEmployeeById( @PathVariable  int employeeId) {
		EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
	 
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK); 
		 
		
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<ApiResponse> deleteEmployeeById(@PathVariable  int employeeId) {
		
		 if ( employeeId <0 ) {
			 throw new BadRequestException(" Employee Id cannot be negative");
		 }
		 
		ApiResponse  apiResponse = employeeService.deleteEmployeeById(employeeId);
	    return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
		 
		 
		 
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/employees/{employeeId}")
	   public ResponseEntity<ApiResponse> updateEmployeeById(@RequestBody EmployeeDTO employeeDTO,@PathVariable int employeeId ) {
		   
		 
			   ApiResponse  apiResponse =    employeeService.updateEmployeeById(employeeDTO,employeeId ); 
			   return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
	 
		 
	   }
	
	
}
