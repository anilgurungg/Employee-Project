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
import com.dto.TicketDTO;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.TicketService;

@RequestMapping("/api/v2/projects/{projectId}")
@RestController
public class TicketController {
	@Autowired
	TicketService service;
	 
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/tickets")
	   public ResponseEntity<String> addTicket(@PathVariable(name = "projectId") int projectId, @RequestBody TicketDTO ticketDTO, @CurrentUser UserPrincipal currentUser) {
		service.addTicket(projectId,ticketDTO,currentUser);
		   
		   return new ResponseEntity<>("Ticket added successfully", HttpStatus.CREATED);  
		
	   }
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/tickets")
	public ResponseEntity<List<TicketDTO>> getAllTickets(@PathVariable(name = "projectId") int projectId) {
		List<TicketDTO> listOfTickets = service.getAllTickets(projectId);
		return new ResponseEntity<>(listOfTickets, HttpStatus.OK); 
	 
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/tickets/me")
	public ResponseEntity<List<TicketDTO>> getTicketById(@PathVariable(name = "projectId") int projectId, @CurrentUser UserPrincipal currentUser) {
		List<TicketDTO> listOfTickets = service.getMyTickets(projectId, currentUser);
		return new ResponseEntity<>(listOfTickets, HttpStatus.OK); 
	 
	}
	
	@GetMapping("/tickets/{ticketId}")
	public ResponseEntity<TicketDTO> getTicketById(@PathVariable(name = "projectId") int projectId,@PathVariable(name = "ticketId") int ticketId, @CurrentUser UserPrincipal currentUser) {
		TicketDTO ticketDTO = service.getTicketById(projectId, ticketId, currentUser);
		return new ResponseEntity<>(ticketDTO, HttpStatus.OK); 
	 
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/tickets/{ticketId}")
	public ResponseEntity<ApiResponse> deleteTicketById(@PathVariable(name = "projectId") int projectId,@PathVariable(name = "ticketId") int ticketId, @CurrentUser UserPrincipal currentUser) {
		ApiResponse  apiResponse  = service.deleteTicketById(projectId, ticketId, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
	 
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/tickets/{ticketId}")
	   public ResponseEntity<ApiResponse> updateTicketById(@PathVariable(name = "projectId") int projectId,@PathVariable(name = "ticketId") int ticketId, @RequestBody TicketDTO ticketDTO ,    @CurrentUser UserPrincipal currentUser) {
		   
		 
			   ApiResponse  apiResponse =    service.updateTicketById(projectId, ticketId,ticketDTO,currentUser ); 
			   return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
	 
		 
	   }
}
