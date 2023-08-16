package com.security;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.entity.EmployeeEntity;

public class UserPrincipal implements UserDetails {
	private int employeeId;
	private String employeeName;
	private String emailId;
	private String password;
	 

	private Collection<? extends GrantedAuthority> authorities;
	
	
	
	public UserPrincipal(int employeeId, String employeeName, String emailId, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.emailId = emailId;
		this.password = password;

		if (authorities == null) {
			this.authorities = null;
		} else {
			this.authorities = new ArrayList<>(authorities);
		}
	}

	public static UserPrincipal create(EmployeeEntity user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
		user.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)) );
		
		return new UserPrincipal(user.getEmployeeId(),user.getEmployeeName(), user.getEmailId(), user.getPassword(), authorities);
	}
	
	
	
	public int getEmployeeId() {
		return employeeId;
	}
	
	

	public String getEmailId() {
		return emailId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities == null ? null : new ArrayList<>(authorities);
	}

	@Override
	public String getPassword() {
		return password;
	}
 

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) object;
		return Objects.equals(employeeId, that.employeeId);
	}

	public int hashCode() {
		return Objects.hash(employeeId);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

}
