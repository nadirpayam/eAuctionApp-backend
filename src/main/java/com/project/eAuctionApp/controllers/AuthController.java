package com.project.eAuctionApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.eAuctionApp.entities.User;
import com.project.eAuctionApp.requests.RegisterRequest;
import com.project.eAuctionApp.requests.UserRequest;
import com.project.eAuctionApp.security.JwtTokenProvider;
import com.project.eAuctionApp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private UserService userService;
	private PasswordEncoder passwordEncoder;

	private AuthenticationManager authenticationManager;

	private JwtTokenProvider jwtTokenProvider;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		super();
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public String login(@RequestBody UserRequest loginRequest) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);
		return "Bearer " + jwtToken;

	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
		if (userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
			return new ResponseEntity<>("username var knk zaten", HttpStatus.BAD_REQUEST);
		} else {
			User user = new User();
			user.setName(registerRequest.getName());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			user.setRole(registerRequest.getRole());
			user.setSurName(registerRequest.getSurName());
			user.setUserName(registerRequest.getUserName());
			userService.saveOneUser(user);
			return new ResponseEntity<>("user oldu be ", HttpStatus.CREATED);
		}
	}

}
