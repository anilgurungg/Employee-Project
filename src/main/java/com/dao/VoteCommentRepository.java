package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.entity.CommentEntity;
import com.entity.CommentVoteEntity;
import com.entity.EmployeeEntity;

public interface VoteCommentRepository extends JpaRepository<CommentVoteEntity, Integer> {

	CommentVoteEntity findByCommentAndEmployee(CommentEntity commentEntity, EmployeeEntity employeeEntity);
//	@Modifying
//    @Query("DELETE FROM CommentVoteEntity cve WHERE cve.employeeId = ?1 AND cve.commentId = ?2")
     void deleteByEmployeeEmployeeIdAndCommentCommentId(int employeeId, int commentId);

}
