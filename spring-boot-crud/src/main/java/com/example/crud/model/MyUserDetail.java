package com.example.crud.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyUserDetail implements UserDetails {
	String username;
	public String password;
	public boolean active;
	public String Role;
	private int id;
	private String email;
	private List<GrantedAuthority> authorities;
	
	BCryptPasswordEncoder encoder = passwordEncoder();
	public MyUserDetail(User user) {
		this.username = user.userName;
		this.password = encoder.encode(user.getPassword());
		this.active = user.active;
		this.email = user.email;
		this.authorities = Arrays.stream(user.getRole().split(","))
				.map(SimpleGrantedAuthority :: new)
				.collect(Collectors.toList());
	}
	
	private BCryptPasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}

	public MyUserDetail() {}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return active;
	}

}
