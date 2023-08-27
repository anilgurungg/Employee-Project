package com.service;

import javax.validation.Valid;

import com.dto.ApiResponse;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.security.UserPrincipal;

public interface ProjectService {

	void addProject(ProjectDTO projectDTO, UserPrincipal currentUser);

	PagedResponseDTO<ProjectDTO> getAllProjects(Integer page, Integer size);

	ProjectDTO getProjectById(int projectId, UserPrincipal currentUser);

	ApiResponse deleteProjectById(int projectId, UserPrincipal currentUser);

	ProjectDTO addAsigneeToProject(int projectId, int employeeId);

	ProjectDTO removeAsigneeFromProject(int projectId, int employeeId);

	PagedResponseDTO<ProjectDTO> getMyProjects(Integer page, Integer size, UserPrincipal currentUser);

	ApiResponse editProjectById(int projectId, @Valid ProjectDTO projectDTO, UserPrincipal currentUser);

}
