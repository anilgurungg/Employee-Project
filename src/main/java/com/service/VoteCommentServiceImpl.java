package com.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.CommentRepository;
import com.dao.EmployeeRepository;
import com.dao.VoteCommentRepository;
import com.dto.CommentDTO;
import com.dto.CommentVoteDTO;
import com.entity.CommentEntity;
import com.entity.CommentVoteEntity;
import com.entity.EmployeeEntity;
import com.entity.VoteType;
import com.exception.BadRequestException;
import com.exception.ResourceNotFoundException;
import com.exception.UnAuthorizedException;
import com.security.UserPrincipal;

@Service
@Transactional
public class VoteCommentServiceImpl implements VoteCommentService {

	@Autowired
	VoteCommentRepository voteCommentRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private ModelMapper modelMapper;

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
		List<CommentVoteDTO> voteDTOs = commentEntity.getVotes().stream().map(voteEntity -> {
			CommentVoteDTO voteDTO = new CommentVoteDTO();
			voteDTO.setVoteId(voteEntity.getVoteId());
			voteDTO.setEmployeeId(voteEntity.getEmployee().getEmployeeId());
			voteDTO.setCommentId(voteEntity.getComment().getCommentId());
			voteDTO.setVoteType(voteEntity.getVoteType());
			return voteDTO;
		}).collect(Collectors.toList());
		commentDTO.setVotes(voteDTOs);
		return commentDTO;
	}

	@Override
	public CommentDTO vote(int commentId, CommentVoteDTO voteCommentDTO, UserPrincipal currentUser) {
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));

		EmployeeEntity employeeEntity = employeeRepository.findById(currentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EMPLOYEE", "ID", currentUser.getEmployeeId()));

		CommentVoteEntity existingVote = voteCommentRepository.findByCommentAndEmployee(commentEntity, employeeEntity);

		if (existingVote != null) {
			if (existingVote.getVoteType() == voteCommentDTO.getVoteType()) {
				throw new BadRequestException(
						"You have already " + voteCommentDTO.getVoteType().name().toLowerCase() + "d this comment");
			}

			existingVote.setVoteType(voteCommentDTO.getVoteType());
			voteCommentRepository.save(existingVote);
		} else {
			CommentVoteEntity newVote = new CommentVoteEntity(commentEntity, employeeEntity,
					voteCommentDTO.getVoteType());
			commentEntity.getVotes().add(newVote);
			voteCommentRepository.save(newVote);
		}

		CommentDTO updatedCommentDTO = convertCommentToDTO(commentEntity);
		return updatedCommentDTO;
	}

	public CommentDTO unvote(int commentId, UserPrincipal currentUser) {
		System.out.println(commentId + currentUser.getEmployeeId());

		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));
		EmployeeEntity employeeEntity = employeeRepository.findById(currentUser.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));
		

		CommentVoteEntity commentVoteEntity = voteCommentRepository.findByCommentAndEmployee(commentEntity,
				employeeEntity);
		
		if (commentVoteEntity == null) {
		    throw new BadRequestException("You have not voted on this comment");
		}

		commentEntity.getVotes().remove(commentVoteEntity);
		employeeEntity.getCommentVotes().remove(commentVoteEntity);

		voteCommentRepository.deleteByEmployeeEmployeeIdAndCommentCommentId(currentUser.getEmployeeId(), commentId);

		CommentDTO updatedCommentDTO = convertCommentToDTO(commentEntity);
		return updatedCommentDTO;

	}

}
