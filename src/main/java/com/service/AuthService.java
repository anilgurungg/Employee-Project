package com.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.dto.ApiResponse;
import com.dto.JwtAuthenticationResponseDTO;
import com.dto.LoginDTO;
import com.dto.SignUpDTO;

public interface AuthService {

	ResponseEntity<ApiResponse> registerEmployee(@Valid SignUpDTO signUpRequest);

	ResponseEntity<JwtAuthenticationResponseDTO> loginEmployee(@Valid LoginDTO signUpRequest);

}
