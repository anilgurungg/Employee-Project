package com.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProjectDTO {
	private int projectId;
	 @NotBlank
	private String name;
	 @NotBlank
	private String description;
//	private Set<EmployeeDTO> employees = new HashSet<>();
	private Set<Integer> assignedEmployeeIds;
	private Instant createdAt;
    private Integer createdBy;
    private Instant updatedAt;
    private Integer updated_by;

}
