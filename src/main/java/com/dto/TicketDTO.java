package com.dto;

import java.util.Set;

import lombok.Data;

@Data
public class TicketDTO {
	private int ticketId;
    private String subject;
    private String description;
    private int projectId;
    private Set<Integer> assignedEmployeeIds;

	
    
}
	
	