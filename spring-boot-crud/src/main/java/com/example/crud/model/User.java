package com.example.crud.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user",uniqueConstraints = { 
		@UniqueConstraint(columnNames = "user_name"),
		@UniqueConstraint(columnNames = "email") 
	})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	@NotBlank
	@Column(name = "user_name")
	public String userName;
	@NotBlank
	@Column(name = "password")
	public String password;
	@Column(name = "active")
	public boolean active;
	@Column(name = "roles")
	public String Role;
	@NotBlank
	@Column(name = "email")
	public String email;
	
	public User() {}
	
	public User(String username,String password,String email) {
		this.userName = username;
		this.password = password;
		this.email = email;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return userName;
	}
	public void setUsername(String username) {
		this.userName = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}

}
