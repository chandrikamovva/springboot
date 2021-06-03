package com.example.crud.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "refreshtoken")
public class RefreshToken {

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private int id;
	  
	  @OneToOne
	  @JoinColumn(name = "id", referencedColumnName = "id")
	  private User user;

	  @Column(nullable = false, unique = true)
	  private String token;

	  @Column(nullable = false)
	  private Instant expiryDate;

	  public RefreshToken() {
	  }
	  
	  public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public Instant getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(Instant expiryDate) {
			this.expiryDate = expiryDate;
		}

	
}
