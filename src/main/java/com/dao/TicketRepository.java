package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.ProjectEntity;
import com.entity.TicketEntity;

@Repository
public interface TicketRepository  extends JpaRepository<TicketEntity, Integer> {

	List<TicketEntity> findByProjectProjectId(Integer projectId);
	
	@Query("SELECT t FROM TicketEntity t WHERE t IN (SELECT DISTINCT t FROM TicketEntity t JOIN t.assignedEmployees e WHERE e.employeeId = :employeeId)")
	List<TicketEntity> findAllTicketsByEmployeeId(@Param("employeeId") int employeeId);


}
