package com.service;

import java.util.List;

import com.dto.ApiResponse;
import com.dto.ProjectDTO;
import com.security.UserPrincipal;

public interface ProjectService {

	void addProject(ProjectDTO projectDTO, UserPrincipal currentUser);

	List<ProjectDTO> getAllProjects();

	ProjectDTO getProjectById(int projectId, UserPrincipal currentUser);

	ApiResponse deleteProjectById(int projectId, UserPrincipal currentUser);

	void addAsigneeToProject(int projectId, int employeeId);

	void removeAsigneeFromProject(int projectId, int employeeId);

	List<ProjectDTO> getMyProjects(UserPrincipal currentUser);

}
