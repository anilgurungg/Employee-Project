package com.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="emp_table")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int employeeId;
	private String employeeName;
    private String emailId;
    private String password;
    private int salary;
	
	
	
	@ManyToMany
	  @JoinTable(
	            name = "employee_role",
	            joinColumns = @JoinColumn(name="employee_id"),
	            inverseJoinColumns = @JoinColumn(name = "role_id")
	    )
	private List<Role> roles;

//	@ManyToMany
//	  @JoinTable(
//	            name = "employee_projects",
//	            joinColumns = @JoinColumn(name="employeeId"),
//	            inverseJoinColumns = @JoinColumn(name = "projectId")
//	    )
//	private List<ProjectEntity> projects;
//	
//	@OneToMany(mappedBy = "employee")
//    private List<TicketEntity> tickets = new ArrayList<>();
	
	@ToString.Exclude
	@JsonIgnore
	@ManyToMany(mappedBy = "employees")
	private Set<ProjectEntity> projects = new HashSet<>();
	
	@ToString.Exclude
	@JsonIgnore
    @ManyToMany(mappedBy = "assignedEmployees")
    private Set<TicketEntity> tickets = new HashSet<>();

	@ElementCollection
    @CollectionTable(name = "employee_permissions", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "permission")
    private Set<String> permissions = new HashSet<>();

 
	
	public EmployeeEntity(String employeeName, String emailId, String password,
			int salary) {
		super();
		
		this.employeeName = employeeName;
		this.emailId = emailId;
		this.password = password;
		this.salary = salary;
	}
		



	public List<Role> getRoles() {

		return roles == null ? null : new ArrayList<>(roles);
	}

	public void setRoles(List<Role> roles) {

		if (roles == null) {
			this.roles = null;
		} else {
			this.roles = Collections.unmodifiableList(roles);
		}
	}
	
	  public void setProjects( Set<ProjectEntity> projects ) {
	    	if ( projects == null ) {
	    		this.projects = null;
	    	} else {
	    		this.projects = projects;
	    	}
	    }
	    
	    public Set<ProjectEntity> getProjectEntities() {
	    	return projects == null ? null : new HashSet<>(projects);
	    }
 
	
}
