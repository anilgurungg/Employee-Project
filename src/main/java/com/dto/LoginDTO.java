package com.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginDTO {
	@NotBlank
	@Email
	 private String emailId;
	@NotBlank
	 private String password;
	 
}
