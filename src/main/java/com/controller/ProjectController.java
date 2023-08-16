package com.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.dto.ApiResponse;
import com.dto.ProjectDTO;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.ProjectService;

@RequestMapping("/api/v2")
@RestController
public class ProjectController {
	@Autowired
	ProjectService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/projects")
	public ResponseEntity<String> addProject(@RequestBody ProjectDTO projectDTO,
			@CurrentUser UserPrincipal currentUser) {
		service.addProject(projectDTO, currentUser);

		return new ResponseEntity<>("Project added successfully", HttpStatus.CREATED);

	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/projects")
	public ResponseEntity<List<ProjectDTO>> getAllProjects() {
		List<ProjectDTO> listOfProjects = service.getAllProjects();
		return new ResponseEntity<>(listOfProjects, HttpStatus.OK);

	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/projects/me")
	public ResponseEntity<List<ProjectDTO>> getProjectByEmployee(@CurrentUser UserPrincipal currentUser) {
		List<ProjectDTO> listOfProjects = service.getMyProjects(currentUser);
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
	@PutMapping("/projects/{projectId}/addAsignee/{employeeId}")
	public ResponseEntity<String> add(@PathVariable int projectId, @PathVariable int employeeId) {
		service.addAsigneeToProject(projectId, employeeId);

		return new ResponseEntity<>("Employeed added to project successfully", HttpStatus.OK);

	}


	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/projects/{projectId}/removeAsignee/{employeeId}")
	public ResponseEntity<String> removeAsigneeFromProject(@PathVariable int projectId, @PathVariable int employeeId) {
		service.removeAsigneeFromProject(projectId, employeeId);

		return new ResponseEntity<>("Employeed removed from project successfully", HttpStatus.OK);

	}

}
