package com.project.eAuctionApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.eAuctionApp.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	RefreshToken findByUserId(Long userId);


}
