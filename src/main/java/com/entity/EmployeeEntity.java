package com.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "emp_table")
@Data
@NoArgsConstructor
public class EmployeeEntity extends AuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employeeId;

	@NotBlank
	private String employeeName;

	@NotBlank
	@Email
	private String emailId;

	@NotBlank
	private String password;
	private int salary;

	@ManyToMany
	@EqualsAndHashCode.Exclude
	@JoinTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@ManyToMany(mappedBy = "employees")
	private Set<ProjectEntity> projects = new HashSet<>();

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@ManyToMany(mappedBy = "assignedEmployees")
	private Set<TicketEntity> tickets = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentVoteEntity> commentVotes = new ArrayList<>();

	public EmployeeEntity(String employeeName, String emailId, String password, int salary) {
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

	public void setProjects(Set<ProjectEntity> projects) {
		if (projects == null) {
			this.projects = null;
		} else {
			this.projects = projects;
		}
	}

	public Set<ProjectEntity> getProjectEntities() {
		return projects == null ? null : new HashSet<>(projects);
	}

	public void setTickets(Set<TicketEntity> tickets) {
		if (tickets == null) {
			this.tickets = null;
		} else {
			this.tickets = tickets;
		}
	}

	public Set<TicketEntity> getTickets() {
		return tickets == null ? null : new HashSet<>(tickets);
	}

}
