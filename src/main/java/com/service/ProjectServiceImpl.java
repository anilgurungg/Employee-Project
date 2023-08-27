package com.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.dao.EmployeeRepository;
import com.dao.ProjectRepository;
import com.dto.ApiResponse;
import com.dto.EmployeeDTO;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.entity.EmployeeEntity;
import com.entity.ProjectEntity;
import com.entity.RoleName;
import com.entity.TicketEntity;
import com.exception.EmployeeAPIException;
import com.exception.ResourceNotFoundException;
import com.exception.UnAuthorizedException;
import com.security.UserPrincipal;
import com.utils.AppUtils;


@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ProjectEntity convertToEntity( ProjectDTO projectDTO ) {
		return modelMapper.map(projectDTO, ProjectEntity.class);
	}
	
	public ProjectDTO convertToDTO( ProjectEntity projectEntity ) {
		return modelMapper.map(projectEntity, ProjectDTO.class);
	}

	public EmployeeDTO convertEmployeeToDTO( EmployeeEntity employeeEntity ) {
		return modelMapper.map(employeeEntity, EmployeeDTO.class);
	}
	
	@Override
	public void addProject(ProjectDTO projectDTO, UserPrincipal currentUser) {
		
		EmployeeEntity employeeEntity = employeeRepository.findById(currentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE", "ID", currentUser.getEmployeeId()));
 
       ProjectEntity projectEntity = convertToEntity(projectDTO);
       try {
    	   System.out.println(projectEntity);
   		projectEntity.getAssignedEmployees().add(employeeEntity);
   		employeeEntity.getProjectEntities().add(projectEntity);
   		System.out.println(projectEntity);
   		employeeRepository.saveAndFlush(employeeEntity);
		projectRepository.saveAndFlush(projectEntity);
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
		
		
		

	}

	@Override
	public PagedResponseDTO<ProjectDTO> getAllProjects(Integer page, Integer size) {
		
		AppUtils.validatePageNumberAndSize(page, size);
		
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
		
		
		Page<ProjectEntity> projectEntities = projectRepository.findAll(pageable);
		
		List<ProjectDTO>  content = projectEntities.getNumberOfElements() == 0 ? Collections.emptyList() : projectEntities.getContent().stream()
				.map(  projectEntity -> convertToDTO(projectEntity))
				.collect(Collectors.toList());
		
		return new PagedResponseDTO<ProjectDTO>( content, projectEntities.getNumber(), projectEntities.getSize(), projectEntities.getTotalElements(),projectEntities.getTotalPages(), projectEntities.isLast() );

		
	}
	
	@Override
	public PagedResponseDTO<ProjectDTO> getMyProjects(Integer page, Integer size,UserPrincipal currentUser) {
AppUtils.validatePageNumberAndSize(page, size);
		
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
		
		
		Page<ProjectEntity> projectEntities = projectRepository.findAllProjectsByEmployeeId(currentUser.getEmployeeId(), pageable);
		
		List<ProjectDTO>  content = projectEntities.getNumberOfElements() == 0 ? Collections.emptyList() : projectEntities.getContent().stream()
				.map(  projectEntity -> convertToDTO(projectEntity))
				.collect(Collectors.toList());
		
		return new PagedResponseDTO<ProjectDTO>( content, projectEntities.getNumber(), projectEntities.getSize(), projectEntities.getTotalElements(),projectEntities.getTotalPages(), projectEntities.isLast() );
	}

	@Override
	public ProjectDTO getProjectById(int projectId, UserPrincipal currentUser) {
		
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		System.out.println( currentUser);
		
		
		if (!isEmployeeAssignedToProject(currentUser.getEmployeeId(), projectId) && !currentUser.getAuthorities().contains( new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())) ) {
			throw new UnAuthorizedException(
					new ApiResponse(Boolean.FALSE, "You are not authorized to view this project"));
		}
		
		ProjectDTO projectDTO = convertToDTO(projectEntity);
		System.out.println( projectDTO);
		projectDTO.setAssignedEmployeeIds(projectEntity.getEmployeeIds());

		System.out.println(projectDTO);
		return projectDTO;
	}

	@Override
	public ApiResponse deleteProjectById(int projectId, UserPrincipal currentUser) {
		if (!projectRepository.existsById(projectId)) {
	        throw new ResourceNotFoundException("Project", "ID", projectId);
	    }
		projectRepository.deleteById(projectId);
		return new ApiResponse(Boolean.TRUE, "You successfully deleted project");

	}

	@Override
	public ProjectDTO addAsigneeToProject(int projectId, int employeeId) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", employeeId));

		if (projectEntity.getEmployeeIds().contains(employeeId)) {
			throw new EmployeeAPIException(HttpStatus.CONFLICT, "Employee is already in the project");
		} 
		
			projectEntity.getEmployees().add(employeeEntity);
			employeeEntity.getProjectEntities().add(projectEntity);
			ProjectEntity  updatedProjectEntity =  projectRepository.save(projectEntity);
			ProjectDTO projectDTO = convertToDTO(updatedProjectEntity);
			projectDTO.setAssignedEmployeeIds(projectEntity.getEmployeeIds());

			
			return projectDTO;

	}

	@Override
	public ProjectDTO removeAsigneeFromProject(int projectId, int employeeId) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", employeeId));

		if (!projectEntity.getEmployeeIds().contains(employeeId)) {
			throw new EmployeeAPIException(HttpStatus.CONFLICT, "Employee is not in the project");
		} 
		projectEntity.getEmployees().remove(employeeEntity);
		employeeEntity.getProjectEntities().remove(projectEntity);
		ProjectEntity  updatedProjectEntity =  projectRepository.save(projectEntity);
		ProjectDTO projectDTO = convertToDTO(updatedProjectEntity);
		projectDTO.setAssignedEmployeeIds(projectEntity.getEmployeeIds());



		
		return projectDTO;
	}

	public boolean isEmployeeAssignedToProject(int employeeId, int projectId) {
		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", employeeId));

		for (ProjectEntity project : employeeEntity.getProjects()) {
			if (project.getProjectId() == projectId) {
				return true; // Employee is assigned to the project
			}
		}

		return false; // Employee is not assigned to the project
	}

	@Override
	public ApiResponse editProjectById(int projectId, @Valid ProjectDTO projectDTO, UserPrincipal currentUser) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		
			projectEntity.setName(projectDTO.getName());
			projectEntity.setDescription(projectDTO.getDescription());
			projectRepository.save(projectEntity);
			return new ApiResponse(Boolean.TRUE, "You successfully updated project");

	}



}
