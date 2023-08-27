package com.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.entity.CommentEntity;

//@Repository
public interface CommentRepository  extends JpaRepository<CommentEntity, Integer> {

	Page<CommentEntity> findByTicketTicketId(int ticketId, Pageable pageable);
	

}
