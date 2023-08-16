package com.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "project_table")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int projectId;
	private String name;
	private String description;
	
//	@ManyToMany(mappedBy = "projects")
//    private Set<EmployeeEntity> assignees = new HashSet<>();
//	
//	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TicketEntity> tickets = new ArrayList<>();
	
	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "project")
    private Set<TicketEntity> tickets = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    	@JoinTable(name = "project_employee",
               joinColumns = @JoinColumn(name = "project_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeEntity> employees = new HashSet<>();
	
    



    public ProjectEntity(String name, String description) {
	super();
	this.name = name;
	this.description = description;
    }	
    
    
	
//    public void setAssignEmployees( Set<EmployeeEntity> employeeEntities ) {
//    	if ( employeeEntities == null ) {
//    		this.employees = null;
//    	} else {
//    		this.employees = employeeEntities;
//    	}
//    }
    
//    public Set<EmployeeEntity> getAssignedEmployees() {
//    	return employees == null ? null : new HashSet<>(employees);
//    }
    
    
    public Set<Integer> getEmployeeIds() {
    	Set<Integer> employeeIds = new HashSet<Integer>();
    	if ( !employees.isEmpty() ) {
    	for ( EmployeeEntity temp:employees ) {
    		employeeIds.add(temp.getEmployeeId());
    	}
    	} 
    		return employeeIds;
    }





}
