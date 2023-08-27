package com.service;

import java.util.List;

import javax.validation.Valid;

import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.PagedResponseDTO;
import com.dto.ResponseDTO;
import com.security.UserPrincipal;

public interface EmployeeService {

	EmployeeDTO registerEmp(EmployeeDTO employeeDTO);

	PagedResponseDTO<EmployeeDTO> getAllEmployees(Integer page, Integer size);

	EmployeeDTO getEmployeeById(int employeeId);

	ApiResponse deleteEmployeeById(int employeeId, UserPrincipal currentUser);


	List<EmployeeDTO> searchEmployees(String searchTerm);

	EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO, int employeeId, UserPrincipal currentUser);

}
