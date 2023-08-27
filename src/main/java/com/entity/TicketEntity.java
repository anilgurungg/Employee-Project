package com.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.entity.ProjectEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.lang.Collections;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ticket_table")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ticketId;
    
    @NotBlank
    @Column(length = 1000) 
    private String subject;
    @NotBlank
    @Column(length = 1000) 
    private String description;
  
//    (cascade = CascadeType.ALL)
    @ToString.Exclude
    @ManyToMany
    @EqualsAndHashCode.Exclude 
    @JoinTable(name = "ticket_employee",
               joinColumns = @JoinColumn(name = "ticket_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeEntity> assignedEmployees;
    
    @ToString.Exclude
    @ManyToOne
    @EqualsAndHashCode.Exclude 
    @JoinColumn(name = "project_id")
    private ProjectEntity project;
    
    @JsonIgnore
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments;
    
    
    
    public void setAssignEmployees( Set<EmployeeEntity> employeeEntities ) {
    	if ( employeeEntities == null ) {
    		this.assignedEmployees = null;
    	} else {
    		this.assignedEmployees = employeeEntities;
    	}
    }
    
//    public Set<EmployeeEntity> getAssignedEmployees() {
//    	return assignedEmployees == null ? null : new HashSet<>(assignedEmployees);
//    }
    
    public Set<Integer> getAssignedEmployeeIds() {
    	Set<Integer> empIdSet = new HashSet<Integer>();
    	for ( EmployeeEntity temp:assignedEmployees ) {
    		empIdSet.add(temp.getEmployeeId());
    	}
    	
    	return empIdSet;
    }

	
	

     
}
