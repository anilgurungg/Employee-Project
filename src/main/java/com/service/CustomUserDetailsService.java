package com.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService {
	UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException;

	UserDetails loadUserById(int id);

}
