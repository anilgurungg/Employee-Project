package com.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.ProjectEntity;
//@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
	
	@Query("SELECT DISTINCT p FROM ProjectEntity p JOIN p.employees e WHERE e.employeeId = :employeeId")
    Page<ProjectEntity> findAllProjectsByEmployeeId(@Param("employeeId") int employeeId, Pageable pageable);
}
