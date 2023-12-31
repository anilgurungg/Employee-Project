package com.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.ProjectService;
import com.utils.AppConstants;

@RequestMapping("/api/v2")
@RestController
public class ProjectController {
	@Autowired
	ProjectService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/projects")
	public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDTO projectDTO,
			@CurrentUser UserPrincipal currentUser) {
		service.addProject(projectDTO, currentUser);

		return new ResponseEntity<>("Project added successfully", HttpStatus.CREATED);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/projects")
	public ResponseEntity<PagedResponseDTO<ProjectDTO>> getAllProjects(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponseDTO<ProjectDTO> listOfProjects = service.getAllProjects(page, size);
		return new ResponseEntity<>(listOfProjects, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/projects/me")
	public ResponseEntity<PagedResponseDTO<ProjectDTO>> getMyProjects(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@CurrentUser UserPrincipal currentUser) {
		PagedResponseDTO<ProjectDTO> listOfProjects = service.getMyProjects(page, size, currentUser);
		return new ResponseEntity<>(listOfProjects, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/projects/{projectId}")
	public ResponseEntity<ProjectDTO> getProjectById(@PathVariable(name = "projectId") int projectId,
			@CurrentUser UserPrincipal currentUser) {
		ProjectDTO projectDTO = service.getProjectById(projectId, currentUser);
		return new ResponseEntity<>(projectDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/projects/{projectId}")
	public ResponseEntity<ApiResponse> deleteProjectById(@PathVariable int projectId,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = service.deleteProjectById(projectId, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);

	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/projects/{projectId}")
	public ResponseEntity<ApiResponse> editProjectById(@PathVariable int projectId, @Valid @RequestBody ProjectDTO projectDTO,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = service.editProjectById(projectId,projectDTO, currentUser);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/projects/{projectId}/addAsignee/{employeeId}")
	public ResponseEntity<ProjectDTO> add(@PathVariable int projectId, @PathVariable int employeeId) {
		ProjectDTO updadeProjectDTO = service.addAsigneeToProject(projectId, employeeId);

		return new ResponseEntity<>(updadeProjectDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/projects/{projectId}/removeAsignee/{employeeId}")
	public ResponseEntity<ProjectDTO> removeAsigneeFromProject(@PathVariable int projectId,
			@PathVariable int employeeId) {
		ProjectDTO updadeProjectDTO = service.removeAsigneeFromProject(projectId, employeeId);

		return new ResponseEntity<>(updadeProjectDTO, HttpStatus.OK);

	}

}
