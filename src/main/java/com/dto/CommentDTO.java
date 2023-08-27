package com.dto;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CommentDTO {
	
	private int commentId;
	 @NotBlank
    private String text;
    private Integer ticketId;
    private Integer projectId;
    private Instant createdAt;
    private Integer createdBy;
    private Instant updatedAt;
    private Integer updated_by;
    private Integer netVotes;
    private List<CommentVoteDTO> votes;
	

}
