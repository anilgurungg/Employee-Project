package com.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.EmployeeEntity;

//@Repository
public interface EmployeeRepository  extends JpaRepository<EmployeeEntity, Integer> {

	Optional<EmployeeEntity> findByEmailId(@NotBlank String emailId);

	Boolean existsByEmailId(@NotBlank String emailId);
	
	@Query("SELECT e FROM EmployeeEntity e WHERE e.employeeName LIKE %:searchTerm% OR e.emailId LIKE %:searchTerm%")
	List<EmployeeEntity> searchEmployees(@Param("searchTerm")String searchTerm);
	

}
