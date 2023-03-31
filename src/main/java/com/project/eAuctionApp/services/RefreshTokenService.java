package com.project.eAuctionApp.services;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import com.project.eAuctionApp.entities.RefreshToken;
import com.project.eAuctionApp.entities.User;
import com.project.eAuctionApp.repositories.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Value("${refresh.token.expires.in}")
	Long expireSeconds;

	private RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public boolean isRefreshExpired(RefreshToken token) {
		return token.getExpiryDate().before(new Date());
	}

	public String createRefreshToken(User user) {
		RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
		if (token == null) {
			token = new RefreshToken();
			token.setUser(user);
		}
		token.setToken(UUID.randomUUID().toString());
		token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
		refreshTokenRepository.save(token);
		return token.getToken();
	}

	public RefreshToken getByUser(Long userId) {
		return refreshTokenRepository.findByUserId(userId);	
	}

}
