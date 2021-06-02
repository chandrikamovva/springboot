package com.example.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.crud.model.MyUserDetail;
import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;

@Service
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> user =  userRepository.findByUserName(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found" +username));
		
		return user.map(MyUserDetail:: new).get();
	}
	
	
}
