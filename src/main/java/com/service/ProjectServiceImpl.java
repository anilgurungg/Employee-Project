package com.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
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

	@Override
	public void addProject(ProjectDTO projectDTO, UserPrincipal currentUser) {
		
		EmployeeEntity employeeEntity = employeeRepository.findById(currentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE", "ID", currentUser.getEmployeeId()));
       
		ProjectEntity projectEntity = convertToEntity(projectDTO);
		System.out.println(projectDTO);
		projectEntity.getEmployees().add(employeeEntity);
		System.out.println(projectEntity);
		projectRepository.save(projectEntity);

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

		if (!isEmployeeAssignedToProject(currentUser.getEmployeeId(), projectId) && !currentUser.getAuthorities().contains( new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())) ) {
			throw new UnAuthorizedException(
					new ApiResponse(Boolean.FALSE, "You are not authorized to view this project"));
		}

		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		
		return convertToDTO(projectEntity);
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
	public void addAsigneeToProject(int projectId, int employeeId) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", employeeId));

		if (projectEntity.getEmployeeIds().contains(employeeId)) {
			throw new EmployeeAPIException(HttpStatus.CONFLICT, "Employee is already in the project");
		} else {
			projectEntity.getEmployees().add(employeeEntity);
			employeeEntity.getProjectEntities().add(projectEntity);
			projectRepository.save(projectEntity);
		}

	}

	@Override
	public void removeAsigneeFromProject(int projectId, int employeeId) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "ID", projectId));
		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", employeeId));

		if (!projectEntity.getEmployeeIds().contains(employeeId)) {
			throw new EmployeeAPIException(HttpStatus.CONFLICT, "Employee is not in the project");
		} else {
			projectEntity.getEmployees().remove(employeeEntity);
			employeeEntity.getProjectEntities().remove(projectEntity);
			projectRepository.save(projectEntity);
		}
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



}
