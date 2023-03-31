package com.project.eAuctionApp.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PatchMapping;

import com.project.eAuctionApp.entities.Product;
import com.project.eAuctionApp.entities.User;
import com.project.eAuctionApp.exceptions.UserNotFoundException;
import com.project.eAuctionApp.responses.UserResponse;
import com.project.eAuctionApp.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User newUser) {
		User user = userService.saveOneUser(newUser);
		if(user != null) 
			return new ResponseEntity<>(HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/{userId}")
	public UserResponse getOneUser(@PathVariable Long userId) {
		User user = userService.getOneUserById(userId);
		if(user == null) {
			throw new UserNotFoundException();
		}
		return new UserResponse(user); 
	}

	@PatchMapping("/{userId}")
	public User updateUserFields(@PathVariable Long userId,@RequestBody Map<String, Object> fields) {
		return userService.updateUserByFields(userId, fields);
	}

	
	
	@DeleteMapping("/{userId}")
	public void deleteOneUser(@PathVariable Long userId) {
		userService.deleteById(userId);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private void handleUserNotFound() {
		
	}
	

}