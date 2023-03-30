package com.project.eAuctionApp.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.project.eAuctionApp.entities.Product;
import com.project.eAuctionApp.entities.User;
import com.project.eAuctionApp.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User saveOneUser(User newUser) {
		return userRepository.save(newUser);
	}

	public ResponseEntity<User> getOneUser(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			return ResponseEntity.ok().body(optionalUser.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
		}
	}

	public void deleteById(Long userId) {
		userRepository.deleteById(userId);
	}

	public User updateUserByFields(Long userId, Map<String, Object> fields) {
		Optional<User> existingProduct = userRepository.findById(userId);

		if (existingProduct.isPresent()) {
			fields.forEach((key, value) -> {
				Field field = ReflectionUtils.findField(User.class, key);
				field.setAccessible(true);
				ReflectionUtils.setField(field, existingProduct.get(), value);
			});
			return userRepository.save(existingProduct.get());
		}
		return null;
	}

	public User getOneUserByUserName(String userName) {
		return userRepository.findByUserName(userName);

	}

}
