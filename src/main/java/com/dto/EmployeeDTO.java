package com.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDTO {
	private int employeeId;
	private String employeeName;
	private String emailId;
	private String password;
	private int salary;
	private Set<TicketDTO> tickets = new HashSet<>();
	
	
	public EmployeeDTO(int employeeId, String employeeName, String emailId, String password, int salary,
			Set<TicketDTO> tickets) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.emailId = emailId;
		this.password = password;
		this.salary = salary;
		this.tickets = tickets;
	}

	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}

	
	
	

	
	
	
}
	
	