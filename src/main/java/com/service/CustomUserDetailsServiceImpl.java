package com.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dao.EmployeeRepository;
import com.entity.EmployeeEntity;
import com.security.UserPrincipal;
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	@Transactional
	public UserDetails loadUserById(int id) {
		EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with id: %s", id)));
		return UserPrincipal.create(employee);
	}

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		EmployeeEntity employee = employeeRepository.findByEmailId(emailId)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with this username or email: %s", emailId)));
		return UserPrincipal.create(employee);
	}

}
