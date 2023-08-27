package com.service;

import java.io.Console;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dao.EmployeeRepository;
import com.dao.RoleRepository;
import com.dto.ApiResponse;
import com.dto.JwtAuthenticationResponseDTO;
import com.dto.LoginDTO;
import com.dto.SignUpDTO;
import com.entity.EmployeeEntity;
import com.entity.Role;
import com.entity.RoleName;
import com.exception.AppException;
import com.exception.EmployeeAPIException;
import com.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {
	private static final String EMPLOYEE_ROLE_NOT_SET = "Employee role not set";
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	

	@Override
	public ResponseEntity<ApiResponse> registerEmployee(@Valid SignUpDTO signUpRequest) {
		if (Boolean.TRUE.equals(employeeRepository.existsByEmailId(signUpRequest.getEmailId()))) {
			throw new EmployeeAPIException(HttpStatus.BAD_REQUEST, "Email is already taken");
		}
		
 
		String employeeName = signUpRequest.getEmployeeName().toLowerCase();

	  

		String emailId = signUpRequest.getEmailId().toLowerCase();

		String password = passwordEncoder.encode(signUpRequest.getPassword());
		
		int salary = signUpRequest.getSalary();

		EmployeeEntity employee = new EmployeeEntity(employeeName,emailId,password, salary);
		System.out.println(employee);
		List<Role> roles = new ArrayList<>();
		
		if (employeeRepository.count() == 0) {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(EMPLOYEE_ROLE_NOT_SET)));
			roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
					.orElseThrow(() -> new AppException(EMPLOYEE_ROLE_NOT_SET)));
		} else {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(EMPLOYEE_ROLE_NOT_SET)));
		}

		employee.setRoles(roles);
	
		
			System.out.println(employee);
			EmployeeEntity result = employeeRepository.saveAndFlush(employee) ;
			System.out.println(result);
			URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
					.buildAndExpand(result.getEmployeeId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
	}



	@Override
	public ResponseEntity<?> loginEmployee(@Valid LoginDTO loginDTO) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDTO.getEmailId(), loginDTO.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtTokenProvider.generateToken(authentication);
			return ResponseEntity.ok(new JwtAuthenticationResponseDTO(jwt));
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		            .body(new ApiResponse(Boolean.FALSE, "Invalid email or password"));
		}
		
	}

	
}
