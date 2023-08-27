package com.service;

import com.dto.CommentDTO;
import com.dto.CommentVoteDTO;
import com.entity.VoteType;
import com.security.UserPrincipal;

public interface VoteCommentService {

	CommentDTO vote(int commentId, CommentVoteDTO voteCommentDTO, UserPrincipal currentUser);


	CommentDTO unvote(int commentId, UserPrincipal currentUser);
	
}
