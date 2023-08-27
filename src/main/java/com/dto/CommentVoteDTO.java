package com.dto;

import com.entity.VoteType;

import lombok.Data;

@Data
public class CommentVoteDTO {
	  private Integer voteId;
	 private VoteType voteType;
	 private Integer employeeId;
	 private Integer commentId; 

}
