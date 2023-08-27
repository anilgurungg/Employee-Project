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
import com.dto.CommentDTO;
import com.dto.PagedResponseDTO;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.CommentService;
import com.utils.AppConstants;

@RequestMapping("/api/v2/projects/{projectId}/tickets/{ticketId}")
@RestController
public class CommentController {
	@Autowired
	CommentService service;
	 
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/comments")
	   public ResponseEntity<CommentDTO> addComment(
			   @PathVariable(name = "projectId") int projectId, 
			   @PathVariable(name = "ticketId") int ticketId,
			   @Valid  @RequestBody CommentDTO commentDTO, 
			   @CurrentUser UserPrincipal currentUser) {
	  CommentDTO response = service.addComment(projectId, ticketId, commentDTO, currentUser);
		   
		   return new ResponseEntity<>(response, HttpStatus.CREATED);  
		
	   }
//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/tickets/all")
//	public ResponseEntity<PagedResponseDTO<TicketDTO>> getAllTickets(
//			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
//			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
//	@PathVariable(name = "projectId") int projectId) {
//		PagedResponseDTO<TicketDTO> listOfTickets = service.getAllTickets(page, size, projectId);
//		return new ResponseEntity<>(listOfTickets, HttpStatus.OK); 
//	 
//	}
	
//	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/comments")
	public ResponseEntity<PagedResponseDTO<CommentDTO>> getCommentsByTicket(
					@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
					@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
			,@PathVariable(name = "projectId") int projectId 
			,@PathVariable(name = "ticketId") int ticketId, 
			@CurrentUser UserPrincipal currentUser) {
		PagedResponseDTO<CommentDTO> listOfComments = service.getCommentsByTicket(page, size,projectId,ticketId, currentUser);
		return new ResponseEntity<>(listOfComments, HttpStatus.OK); 
	 
	}
//	
	@GetMapping("/comments/{commentId}")
	public ResponseEntity<CommentDTO> getCommentById(
			@PathVariable(name = "commentId") int commentId,
			@PathVariable(name = "ticketId") int ticketId,
			@CurrentUser UserPrincipal currentUser) 
	{		
		CommentDTO commentDTO = service.getCommentById(commentId, ticketId, currentUser);
		return new ResponseEntity<>(commentDTO, HttpStatus.OK); 
	 
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteCommentById(
			@PathVariable(name = "commentId") int commentId,
			@PathVariable(name = "ticketId") int ticketId, 
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse  apiResponse  = service.deleteCommentById(commentId, ticketId, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
	 
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentDTO> updateCommentById(
			@PathVariable(name = "projectId") int projectId,
			@PathVariable(name = "commentId") int commentId,
			@PathVariable(name = "ticketId") int ticketId, 
			@Valid @RequestBody CommentDTO commentDTO, 
			@CurrentUser UserPrincipal currentUser) {
		CommentDTO  response  = service.updateCommentById(projectId, commentId, ticketId, commentDTO, currentUser);
		return new ResponseEntity<>(response, HttpStatus.OK); 
	 
	}

}
