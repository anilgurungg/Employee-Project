package com.service;

import com.dto.ApiResponse;
import com.dto.CommentDTO;
import com.dto.PagedResponseDTO;
import com.security.UserPrincipal;

public interface CommentService {

	CommentDTO addComment(int projectId, int ticketId, CommentDTO commentDTO, UserPrincipal currentUser);

	PagedResponseDTO<CommentDTO> getCommentsByTicket(Integer page, Integer size, int projectId, int ticketId,
			UserPrincipal currentUser);

	CommentDTO getCommentById(int commentId, int ticketId, UserPrincipal currentUser);

	ApiResponse deleteCommentById(int commentId, int ticketId, UserPrincipal currentUser);

	CommentDTO updateCommentById(int projectId,int commentId, int ticketId, CommentDTO commentDTO, UserPrincipal currentUser);

}
