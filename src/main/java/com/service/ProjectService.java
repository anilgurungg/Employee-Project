package com.service;

import com.dto.ApiResponse;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.security.UserPrincipal;

public interface ProjectService {

	void addProject(ProjectDTO projectDTO, UserPrincipal currentUser);

	PagedResponseDTO<ProjectDTO> getAllProjects(Integer page, Integer size);

	ProjectDTO getProjectById(int projectId, UserPrincipal currentUser);

	ApiResponse deleteProjectById(int projectId, UserPrincipal currentUser);

	void addAsigneeToProject(int projectId, int employeeId);

	void removeAsigneeFromProject(int projectId, int employeeId);

	PagedResponseDTO<ProjectDTO> getMyProjects(Integer page, Integer size, UserPrincipal currentUser);

}
