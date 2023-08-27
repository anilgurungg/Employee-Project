package com.controller;

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
import com.dto.TicketDTO;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.TicketService;
import com.utils.AppConstants;

@RequestMapping("/api/v2/projects/{projectId}")
@RestController
public class TicketController {
	@Autowired
	TicketService service;

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/tickets")
	public ResponseEntity<String> addTicket(@PathVariable(name = "projectId") int projectId,
			@Valid 	@RequestBody TicketDTO ticketDTO, @CurrentUser UserPrincipal currentUser) {
		service.addTicket(projectId, ticketDTO, currentUser);

		return new ResponseEntity<>("Ticket added successfully", HttpStatus.CREATED);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/tickets/all")
	public ResponseEntity<PagedResponseDTO<TicketDTO>> getAllTickets(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "projectId") int projectId) {
		PagedResponseDTO<TicketDTO> listOfTickets = service.getAllTickets(page, size, projectId);
		return new ResponseEntity<>(listOfTickets, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/tickets")
	public ResponseEntity<PagedResponseDTO<TicketDTO>> getMyTickets(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "projectId") int projectId, @CurrentUser UserPrincipal currentUser) {
		PagedResponseDTO<TicketDTO> listOfTickets = service.getMyTickets(page, size, projectId, currentUser);
		return new ResponseEntity<>(listOfTickets, HttpStatus.OK);

	}

	@GetMapping("/tickets/{ticketId}")
	public ResponseEntity<TicketDTO> getTicketById(@PathVariable(name = "projectId") int projectId,
			@PathVariable(name = "ticketId") int ticketId, @CurrentUser UserPrincipal currentUser) {
		TicketDTO ticketDTO = service.getTicketById(projectId, ticketId, currentUser);
		return new ResponseEntity<>(ticketDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/tickets/{ticketId}")
	public ResponseEntity<ApiResponse> deleteTicketById(@PathVariable(name = "projectId") int projectId,
			@PathVariable(name = "ticketId") int ticketId, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = service.deleteTicketById(projectId, ticketId, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/tickets/{ticketId}")
	public ResponseEntity<ApiResponse> updateTicketById(@PathVariable(name = "projectId") int projectId,
			@PathVariable(name = "ticketId") int ticketId,@Valid  @RequestBody TicketDTO ticketDTO,
			@CurrentUser UserPrincipal currentUser) {

		ApiResponse apiResponse = service.updateTicketById(projectId, ticketId, ticketDTO, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/tickets/{ticketId}/addAsignee/{employeeId}")
	public ResponseEntity<TicketDTO> add(@PathVariable int projectId, @PathVariable int employeeId,
			@PathVariable int ticketId) {
		System.out.println("here");
		TicketDTO updadeTicketDTO = service.addAsigneeToTicket(projectId, employeeId, ticketId);

		return new ResponseEntity<>(updadeTicketDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/tickets/{ticketId}/removeAsignee/{employeeId}")
	public ResponseEntity<TicketDTO> removeAsigneeFromTicket(@PathVariable int projectId, @PathVariable int employeeId,
			@PathVariable int ticketId) {
		TicketDTO updadeTicketDTO = service.removeAsigneeFromTicket(projectId, employeeId, ticketId);

		return new ResponseEntity<>(updadeTicketDTO, HttpStatus.OK);

	}
}
