package com.project.eAuctionApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.eAuctionApp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);

}
