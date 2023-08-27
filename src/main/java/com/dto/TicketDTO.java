package com.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TicketDTO {
	private int ticketId;
	 @NotBlank
    private String subject;
	 @NotBlank
    private String description;
    private int projectId;
    private Set<Integer> assignedEmployeeIds;
    private Instant createdAt;
    private Integer createdBy;
    private Instant updatedAt;
    private Integer updated_by;

	
    
}
	
	