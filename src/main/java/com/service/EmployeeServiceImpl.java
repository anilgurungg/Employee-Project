package com.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dao.EmployeeRepository;
import com.dao.RoleRepository;
import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.dto.TicketDTO;
import com.entity.EmployeeEntity;
import com.entity.ProjectEntity;
import com.entity.Role;
import com.entity.RoleName;
import com.entity.TicketEntity;
import com.exception.AppException;
import com.exception.ResourceNotFoundException;
import com.exception.UnAuthorizedException;
import com.security.UserPrincipal;
import com.utils.AppUtils;

import javassist.expr.NewArray;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public EmployeeEntity convertEmployeeToEntity( EmployeeDTO employeeDTO ) {
		return modelMapper.map(employeeDTO, EmployeeEntity.class);
	}
	
	public EmployeeDTO convertEmployeeToDTO( EmployeeEntity employeeEntity ) {
		return modelMapper.map(employeeEntity, EmployeeDTO.class);
	}
	
	public TicketEntity convertTicketToEntity( TicketDTO ticketDTO ) {
		return modelMapper.map(ticketDTO, TicketEntity.class);
	}
	
	public TicketDTO convertTicketToDTO( TicketEntity ticketEntity ) {
		return modelMapper.map(ticketEntity, TicketDTO.class);
	}
	

	@Override
	public EmployeeDTO registerEmp(EmployeeDTO employeeDTO) {
		EmployeeEntity employeeEntity =convertEmployeeToEntity(employeeDTO);
//		BeanUtils.copyProperties(employeeDTO, employeeEntity);
		employeeEntity.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow( () -> new AppException("User role cannot be set!")  )
				);
		employeeEntity.setRoles(roles);
		
		
		EmployeeEntity savedEmployeeEntity =  employeeRepository.save(employeeEntity);
		
		return convertEmployeeToDTO(savedEmployeeEntity);
		
		
	}

	@Override
	public PagedResponseDTO<EmployeeDTO> getAllEmployees(Integer page, Integer size) {
		
		AppUtils.validatePageNumberAndSize(page, size);
		
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "emailId");
		
		Page<EmployeeEntity> employeeEntities = employeeRepository.findAll(pageable);
		
		List<EmployeeDTO>  content = employeeEntities.getNumberOfElements() == 0 ? Collections.emptyList() : employeeEntities.getContent().stream()
				.map( employeeEntity -> { 
					EmployeeDTO employeeDTO = convertEmployeeToDTO(employeeEntity);
					Set<TicketDTO> ticketDTOs = employeeEntity.getTickets().stream()
							.map(ticketEntity -> convertTicketToDTO(ticketEntity))
							.collect(Collectors.toSet());
					employeeDTO.setTickets(ticketDTOs);
					return employeeDTO;
				})
				.collect(Collectors.toList());
		
		return new PagedResponseDTO<EmployeeDTO>( content, employeeEntities.getNumber(), employeeEntities.getSize(), employeeEntities.getTotalElements(),employeeEntities.getTotalPages(), employeeEntities.isLast() );

	}
	

	@Override
	public List<EmployeeDTO> searchEmployees(String searchTerm) {
List<EmployeeEntity> employeeEntities = employeeRepository.searchEmployees(searchTerm);
		
		return employeeEntities.stream()
			.map( employeeEntity -> { 
				EmployeeDTO employeeDTO = convertEmployeeToDTO(employeeEntity);
				Set<TicketDTO> ticketDTOs = employeeEntity.getTickets().stream()
						.map(ticketEntity -> convertTicketToDTO(ticketEntity))
						.collect(Collectors.toSet());
				employeeDTO.setTickets(ticketDTOs);
				return employeeDTO;
			})
			.collect(Collectors.toList());
	}


	@Override
	public EmployeeDTO getEmployeeById(int employeeId) {
		 EmployeeEntity  employeeEntity = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE","ID",employeeId));
		 EmployeeDTO employeeDTO = convertEmployeeToDTO(employeeEntity);
			Set<TicketDTO> ticketDTOs = employeeEntity.getTickets().stream()
					.map(ticketEntity -> convertTicketToDTO(ticketEntity))
					.collect(Collectors.toSet());
			employeeDTO.setTickets(ticketDTOs);
			return employeeDTO;
		
	}

	@Override
	public ApiResponse deleteEmployeeById(int employeeId, UserPrincipal currentUser) {
		 EmployeeEntity  employeeEntity = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE","ID",employeeId));
		 
		 if ( employeeEntity.getEmployeeId() != currentUser.getEmployeeId() && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			 ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to delete this account");
			 throw new UnAuthorizedException(apiResponse);
		 }
		 for (TicketEntity ticket : employeeEntity.getTickets()) {
	            ticket.getAssignedEmployees().remove(employeeEntity);
	        }
		 
		 for (ProjectEntity project : employeeEntity.getProjectEntities()) {
	            project.getEmployees().remove(employeeEntity);
	        }

	        // Clear the employee's projects set to prevent cascading changes
		 employeeEntity.getProjectEntities().clear();
		 employeeEntity.getTickets().clear(); 

		 
		employeeRepository.deleteById(employeeId);
		return new ApiResponse(Boolean.TRUE, "You successfully deleted this acccount");
	}




	@Override
	public EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO, int employeeId, UserPrincipal currentUser) {
		 EmployeeEntity  employeeEntity = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE","ID",employeeId));
		 if ( employeeEntity.getEmployeeId() == currentUser.getEmployeeId() || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			employeeEntity.setEmployeeName(employeeDTO.getEmployeeName());
			employeeEntity.setEmailId(employeeDTO.getEmailId());
			employeeEntity.setPassword(passwordEncoder.encode(employeeDTO.getPassword()) );
			employeeEntity.setSalary(employeeDTO.getSalary());
//			 BeanUtils.copyProperties(employeeDTO, employeeEntity);
			employeeRepository.save(employeeEntity);
			 EmployeeDTO responseEmployeeDTO = convertEmployeeToDTO(employeeEntity);
				Set<TicketDTO> ticketDTOs = employeeEntity.getTickets().stream()
						.map(ticketEntity -> convertTicketToDTO(ticketEntity))
						.collect(Collectors.toSet());
				responseEmployeeDTO.setTickets(ticketDTOs);
				return responseEmployeeDTO;
		 };
		 ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to update this account");
		 throw new UnAuthorizedException(apiResponse);
	}



}