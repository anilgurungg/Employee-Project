package com.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.entity.ProjectEntity;

import io.jsonwebtoken.lang.Collections;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_table")
@Data
@NoArgsConstructor
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ticketId;
    
    private String subject;
    private String description;
  
    
    @ManyToMany
    @JoinTable(name = "ticket_employee",
               joinColumns = @JoinColumn(name = "ticket_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeEntity> assignedEmployees;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;
    
    public void setAssignEmployees( Set<EmployeeEntity> employeeEntities ) {
    	if ( employeeEntities == null ) {
    		this.assignedEmployees = null;
    	} else {
    		this.assignedEmployees = employeeEntities;
    	}
    }
    
    public Set<EmployeeEntity> getAssignedEmployees() {
    	return assignedEmployees == null ? null : new HashSet<>(assignedEmployees);
    }
    
    public Set<Integer> getAssignedEmployeeIds() {
    	Set<Integer> empIdSet = new HashSet<Integer>();
    	for ( EmployeeEntity temp:assignedEmployees ) {
    		empIdSet.add(temp.getEmployeeId());
    	}
    	
    	return empIdSet;
    }

	
	

     
}
