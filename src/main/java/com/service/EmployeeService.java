package com.service;

import java.util.List;

import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.PagedResponseDTO;
import com.dto.ResponseDTO;

public interface EmployeeService {

	EmployeeDTO registerEmp(EmployeeDTO employeeDTO);

	PagedResponseDTO<EmployeeDTO> getAllEmployees(Integer page, Integer size);

	EmployeeDTO getEmployeeById(int employeeId);

	ApiResponse deleteEmployeeById(int employeeId);

	ApiResponse updateEmployeeById(EmployeeDTO employeeDTO, int employeeId)  ;

	List<EmployeeDTO> searchEmployees(String searchTerm);

}
