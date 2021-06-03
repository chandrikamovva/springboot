package com.example.crud.payload.response;

import java.io.Serializable;
import java.util.List;

public class AuthenticationResponse implements Serializable {
	

	private String jwt;
	private String username;
	private List<String> roles;
	private String refreshToken;
	
	public AuthenticationResponse(String jwt, String refreshtoken,String username,List<String> roles2) {
        this.jwt = jwt;
        this.username = username;
        this.roles = roles2;
        this.refreshToken = refreshtoken;
    }
	public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
        
	}

    public String getJwt() {
        return jwt;
    }
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
