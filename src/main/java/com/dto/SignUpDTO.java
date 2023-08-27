package com.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignUpDTO {
	@NotBlank
	private String employeeName;
	@NotBlank
	@Email
	private String emailId;
	@NotBlank
	private String password;

	private int salary;

}
