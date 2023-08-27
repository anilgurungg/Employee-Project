package com.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
public class ProjectEntity extends AuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int projectId;
	
	 @NotBlank
	 @Column(length = 1000) 
	private String name;
	 @NotBlank
	 @Column(length = 1000) 
	private String description;
	
//	@ManyToMany(mappedBy = "projects")
//    private Set<EmployeeEntity> assignees = new HashSet<>();
//	
//	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TicketEntity> tickets = new ArrayList<>();
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	@JsonIgnore
	@OneToMany(mappedBy = "project")
    private Set<TicketEntity> tickets = new HashSet<>();
	
	@JsonIgnore
	@EqualsAndHashCode.Exclude 
	@ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    	@JoinTable(name = "project_employee",
               joinColumns = @JoinColumn(name = "project_id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id"))
	private Set<EmployeeEntity> employees;
//    private Set<EmployeeEntity> employees = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments;
	
    



    public ProjectEntity(String name, String description) {
	super();
	this.name = name;
	this.description = description;
    }	
    
    
	
    public void setAssignEmployees( Set<EmployeeEntity> employeeEntities ) {
    	if ( employeeEntities == null ) {
    		this.employees = null;
    	} else {
    		this.employees = employeeEntities;
    	}
    }
    
    public Set<EmployeeEntity> getAssignedEmployees() {
        return employees == null ? new HashSet<>() : new HashSet<>(employees);
    }
      
    
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
