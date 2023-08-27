package com.service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dao.CommentRepository;
import com.dao.EmployeeRepository;
import com.dao.ProjectRepository;
import com.dao.TicketRepository;
import com.dto.ApiResponse;
import com.dto.CommentDTO;
import com.dto.PagedResponseDTO;
import com.dto.ProjectDTO;
import com.dto.TicketDTO;
import com.dto.CommentVoteDTO;
import com.entity.CommentEntity;
import com.entity.EmployeeEntity;
import com.entity.ProjectEntity;
import com.entity.TicketEntity;
import com.exception.ResourceNotFoundException;
import com.exception.UnAuthorizedException;
import com.security.UserPrincipal;
import com.utils.AppUtils;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private ModelMapper modelMapper;

//	public CommentEntity convertCommentToEntity(CommentDTO commentDTO) {
//		return modelMapper.map(commentDTO, CommentEntity.class);
//	}
	
	public CommentEntity convertCommentToEntity(CommentDTO commentDTO) {
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setText(commentDTO.getText());
		return commentEntity;
		}

//	public CommentDTO convertCommentToDTO(CommentEntity commentEntity) {
//		return modelMapper.map(commentEntity, CommentDTO.class);
//	}
//	
	public CommentDTO convertCommentToDTO(CommentEntity commentEntity) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setCommentId(commentEntity.getCommentId());
		 commentDTO.setText(commentEntity.getText());
		 commentDTO.setTicketId(commentEntity.getTicket().getTicketId());
		 commentDTO.setProjectId(commentEntity.getProject().getProjectId());
		 commentDTO.setCreatedAt(commentEntity.getCreatedAt()); 
		 commentDTO.setCreatedBy(commentEntity.getCreatedBy()); 
		 commentDTO.setUpdatedAt(commentEntity.getUpdatedAt());
		commentDTO.setUpdated_by(commentEntity.getUpdated_by()); 
		commentDTO.setNetVotes(commentEntity.getNetVotes());
		List<CommentVoteDTO> voteDTOs = commentEntity.getVotes().stream()
	            .map(voteEntity -> {
	                CommentVoteDTO voteDTO = new CommentVoteDTO();
	                voteDTO.setVoteId(voteEntity.getVoteId());
	                voteDTO.setEmployeeId(voteEntity.getEmployee().getEmployeeId());
	                voteDTO.setCommentId(voteEntity.getComment().getCommentId());
	                voteDTO.setVoteType(voteEntity.getVoteType());
	                return voteDTO;
	            })
	            .collect(Collectors.toList());
	 commentDTO.setVotes(voteDTOs);
		return commentDTO;
	}


	@Override
	public CommentDTO addComment(int projectId, int ticketId, CommentDTO commentDTO, UserPrincipal currentUser) {
		ProjectEntity projectEntity = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("PROJECT", "ID", projectId));

		
		EmployeeEntity employeeEntity = employeeRepository.findById(currentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE", "ID", currentUser.getEmployeeId()));
		TicketEntity ticketEntity = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("TICKET", "ID", ticketId));
		
		CommentEntity commentEntity = convertCommentToEntity(commentDTO);
		commentEntity.setEmployee(employeeEntity);
		commentEntity.setTicket(ticketEntity);
		commentEntity.setProject(projectEntity);
		

		CommentEntity newCommentEntity = commentRepository.save(commentEntity);

		return convertCommentToDTO(newCommentEntity);

	}

	@Override
	public PagedResponseDTO<CommentDTO> getCommentsByTicket(Integer page, Integer size, int projectId, int ticketId,
			UserPrincipal currentUser) {

		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<CommentEntity> commentEntities = commentRepository.findByTicketTicketId(ticketId, pageable);

		List<CommentDTO> content = commentEntities.getNumberOfElements() == 0 ? Collections.emptyList()
				: commentEntities.getContent().stream().map(commentEntity -> {
					CommentDTO commentDTO = convertCommentToDTO(commentEntity);
					 List<CommentVoteDTO> voteDTOs = commentEntity.getVotes().stream()
					            .map(voteEntity -> {
					                CommentVoteDTO voteDTO = new CommentVoteDTO();
					                voteDTO.setVoteId(voteEntity.getVoteId());
					                voteDTO.setEmployeeId(voteEntity.getEmployee().getEmployeeId());
					                voteDTO.setCommentId(voteEntity.getComment().getCommentId());
					                voteDTO.setVoteType(voteEntity.getVoteType());
					                return voteDTO;
					            })
					            .collect(Collectors.toList());
					 commentDTO.setVotes(voteDTOs);
					return commentDTO;
				}).collect(Collectors.toList());

		return new PagedResponseDTO<>(content, commentEntities.getNumber(), commentEntities.getSize(),
				commentEntities.getTotalElements(), commentEntities.getTotalPages(), commentEntities.isLast());

	}

	@Override
	public CommentDTO getCommentById(int commentId, int ticketId, UserPrincipal currentUser) {
		if (!ticketRepository.existsById(ticketId)) {
	        throw new ResourceNotFoundException("Ticket", "ID", ticketId);
	    }
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));
		CommentDTO commentDTO = convertCommentToDTO(commentEntity);	
		return commentDTO;
	}

	@Override
	public ApiResponse deleteCommentById(int commentId, int ticketId, UserPrincipal currentUser) {
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));


		if (commentEntity.getCreatedBy().equals(currentUser.getEmployeeId()) ) {
			commentRepository.deleteById(commentId);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted comment");
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to delete this comment");
		throw new UnAuthorizedException(apiResponse);
	}

	@Override
	@Transactional
	public CommentDTO updateCommentById(int projectId, int commentId, int ticketId, CommentDTO commentDTO, UserPrincipal currentUser) {		
		
		
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));


		if (!commentEntity.getCreatedBy().equals(currentUser.getEmployeeId()) ) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You are not authorized to update this comment");
			throw new UnAuthorizedException(apiResponse);
		}
		try {
			commentEntity.setText(commentDTO.getText());
			CommentEntity newCommentEntity = commentRepository.saveAndFlush(commentEntity);
			return convertCommentToDTO(newCommentEntity);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
		
	}

}
