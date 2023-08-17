package com.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.TicketEntity;

@Repository
public interface TicketRepository  extends JpaRepository<TicketEntity, Integer> {

	Page<TicketEntity> findByProjectProjectId(Integer projectId, Pageable pageable);
	
//	@Query("SELECT t FROM TicketEntity t WHERE t IN (SELECT DISTINCT t FROM TicketEntity t JOIN t.assignedEmployees e WHERE e.employeeId = :employeeId)")
//	Page<TicketEntity> findAllTicketsByEmployeeId(@Param("employeeId") int employeeId, Pageable pageable);
	
	@Query("SELECT t FROM TicketEntity t JOIN t.assignedEmployees e JOIN t.project p WHERE p.projectId = :projectId AND e.employeeId = :employeeId")
	Page<TicketEntity> findAllTicketsByEmployeeIdAndProjectId(@Param("employeeId") int employeeId, @Param("projectId") int projectId, Pageable pageable);



}
