package com.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dao.EmployeeRepository;
import com.dao.RoleRepository;
import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.ProjectDTO;
import com.dto.TicketDTO;
import com.entity.EmployeeEntity;
import com.entity.ProjectEntity;
import com.entity.Role;
import com.entity.RoleName;
import com.entity.TicketEntity;
import com.exception.AppException;
import com.exception.ResourceNotFoundException;

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
	public List<EmployeeDTO> getAllEmployees() {
		List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
		
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
	public ApiResponse deleteEmployeeById(int employeeId) {
		 EmployeeEntity  employeeEntity = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE","ID",employeeId));
 
		employeeRepository.deleteById(employeeId);
		return new ApiResponse(Boolean.TRUE, "You successfully deleted employee");
	}


	@Override
	public ApiResponse updateEmployeeById(EmployeeDTO employeeDTO, int employeeId)  {
		 EmployeeEntity  employeeEntity = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE","ID",employeeId));
 
		EmployeeEntity employeeEntity1 =new EmployeeEntity();
		
		BeanUtils.copyProperties(employeeDTO, employeeEntity1);
		employeeEntity1.setEmployeeId(employeeId);
		employeeRepository.save(employeeEntity1);
		return new ApiResponse(Boolean.TRUE, "You successfully updated employee");
		
		
	}



}