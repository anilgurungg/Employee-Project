package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.CommentDTO;
import com.dto.CommentVoteDTO;
import com.entity.VoteType;
import com.security.CurrentUser;
import com.security.UserPrincipal;
import com.service.VoteCommentService;

@RequestMapping("/api/v2/comments/{commentId}")
@RestController
public class VoteCommentController {
	
	@Autowired
	VoteCommentService service;
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/vote")
	public ResponseEntity<CommentDTO> voteComment(
			@PathVariable(name = "commentId") int commentId,
			@RequestBody CommentVoteDTO voteCommentDTO, 
			@CurrentUser UserPrincipal currentUser) {
		CommentDTO  response  = service.vote( commentId, voteCommentDTO, currentUser);
		return new ResponseEntity<>(response, HttpStatus.OK); 
	 
	}
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/unvote")
	public ResponseEntity<CommentDTO> unVoteComment(
			@PathVariable(name = "commentId") int commentId, 
			@CurrentUser UserPrincipal currentUser) {
		CommentDTO  response  = service.unvote( commentId, currentUser);
		return new ResponseEntity<>(response, HttpStatus.OK); 
	 
	}

}
