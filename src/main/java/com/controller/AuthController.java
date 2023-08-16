package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.ApiResponse;
import com.dto.JwtAuthenticationResponseDTO;
import com.dto.LoginDTO;
import com.dto.SignUpDTO;
import com.service.AuthService;

@RequestMapping("/api/v2/auth")
@RestController
public class AuthController {
	
	@Autowired
	AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpDTO signUpRequest) {
		
		ResponseEntity<ApiResponse> apiResponse = authService.registerEmployee(signUpRequest);
		return apiResponse;
		}
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponseDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
		
		ResponseEntity<JwtAuthenticationResponseDTO> apiResponse = authService.loginEmployee(loginDTO);
		return apiResponse;
		}
}
