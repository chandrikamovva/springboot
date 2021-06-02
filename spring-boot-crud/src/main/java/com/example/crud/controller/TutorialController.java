package com.example.crud.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.ERole;
import com.example.crud.model.MyUserDetail;
import com.example.crud.model.Role;
import com.example.crud.model.Tutorial;
import com.example.crud.model.User;
import com.example.crud.payload.request.AuthenticationRequest;
import com.example.crud.payload.request.SignupRequest;
import com.example.crud.payload.response.AuthenticationResponse;
import com.example.crud.payload.response.MessageResponse;
import com.example.crud.repository.TutorialRepository;
import com.example.crud.repository.UserRepository;
import com.example.crud.util.JwtUtils;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.Tutorial;
import com.example.crud.repository.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController

@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:4200")

//@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/api")
public class TutorialController {
	
	@Autowired
	TutorialRepository tutorialRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;

	
	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			System.out.println("save is calling");
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/")
	public String getAll()
	{
	
		return "welcome";
	}	

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false)String title){
		try {
		   List<Tutorial> tutorials = new ArrayList<Tutorial>();
		   if(title == null) {
			   tutorialRepository.findAll().forEach(tutorials::add);
		   }else {
			   tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
		   }
		   if (tutorials.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }
		
		      return new ResponseEntity<>(tutorials, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") Long id){
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished(){
		try {
		List<Tutorial> tutorial = new ArrayList<Tutorial>();
		tutorial = tutorialRepository.findByPublished(true);
			if(tutorial.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(tutorial,HttpStatus.OK);
			}
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorialById(@PathVariable("id") Long id,@RequestBody Tutorial tutorial){
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial),HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorialById(@PathVariable("id") Long id){
		try {
		      tutorialRepository.deleteById(id);
		      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    } catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}
	
	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAll(){
		try {
		tutorialRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/auth/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		Authentication authentication;
		System.out.println("authenticate request calling");
		try {
			
			 authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			 System.out.println("authenticate request user"+authenticationRequest.getUsername()+"password"+authenticationRequest.getPassword());
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication);
		System.out.println("jwt calling"+jwt);
		MyUserDetail userDetails = (MyUserDetail) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new AuthenticationResponse(jwt,  
				 userDetails.getUsername(),  
				 roles));
		
	}
	
	@PostMapping("/auth/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
		if(userRepository.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if(userRepository.existsByemail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email Already Existed"));
		}
		
		User user = new User(signupRequest.getUsername(),encoder.encode(signupRequest.getPassword()),signupRequest.getEmail());
		
		Set<String> strRoles  = signupRequest.getRole();
		Set<String> roles = new HashSet<>();
		
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add("ROLE_USER");
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> 
							new RuntimeException("Error: Role is not found."));
					
					roles.add("ROLE_ADMIN");

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add("ROLE_MODERATOR");

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add("ROLE_USER");
				}
			});
		}
		 String strNames = String.join(",", roles);
		System.out.println("role calling"+strNames);
		user.setRole(strNames);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
}
