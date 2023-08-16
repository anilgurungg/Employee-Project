package com.dao;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository  extends JpaRepository<EmployeeEntity, Integer> {

	Optional<EmployeeEntity> findByEmailId(@NotBlank String emailId);

	Boolean existsByEmailId(@NotBlank String emailId);

}
