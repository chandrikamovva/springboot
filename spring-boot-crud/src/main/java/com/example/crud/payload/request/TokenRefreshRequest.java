package com.example.crud.payload.request;

import javax.validation.constraints.NotEmpty;

public class TokenRefreshRequest {
	
	@NotEmpty
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
