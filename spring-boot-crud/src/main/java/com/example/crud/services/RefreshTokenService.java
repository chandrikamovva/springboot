package com.example.crud.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.crud.model.RefreshToken;
import com.example.crud.repository.RefreshTokenRepository;
import com.example.crud.repository.UserRepository;
import com.example.crud.util.TokenRefreshException;

@Service
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${crud.app.jwtRefreshExpirationMs}")
	Long refreshTokenDurationMs;
	
	public RefreshToken verifyExpiration(RefreshToken refreshToken) {
		
		if(refreshToken.getExpiryDate().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(refreshToken);
			throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
		}
		return refreshToken;
		
	} 
	
	public Optional<RefreshToken> findByToken(String token){
		return refreshTokenRepository.findByToken(token);
		
	}
	
	public RefreshToken createRefreshToken(int userId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userRepository.findById(userId).get());
	    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());
	    refreshToken = refreshTokenRepository.save(refreshToken);

	    return refreshToken;
		
	}
	
	@Transactional
	  public int deleteByUserId(int userId) {
	    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	  }
}
