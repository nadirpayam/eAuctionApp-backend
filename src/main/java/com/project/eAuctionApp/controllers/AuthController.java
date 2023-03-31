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

import com.project.eAuctionApp.entities.RefreshToken;
import com.project.eAuctionApp.entities.User;
import com.project.eAuctionApp.requests.RefreshRequest;
import com.project.eAuctionApp.requests.RegisterRequest;
import com.project.eAuctionApp.requests.UserRequest;
import com.project.eAuctionApp.responses.AuthResponse;
import com.project.eAuctionApp.security.JwtTokenProvider;
import com.project.eAuctionApp.services.RefreshTokenService;
import com.project.eAuctionApp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private UserService userService;
	private PasswordEncoder passwordEncoder;

	private AuthenticationManager authenticationManager;

	private JwtTokenProvider jwtTokenProvider;

	private RefreshTokenService refreshTokenService;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService,
			JwtTokenProvider jwtTokenProvider) {
		super();
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody UserRequest loginRequest) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwtToken = jwtTokenProvider.generateJwtToken(auth);
		User user = userService.getOneUserByUserName(loginRequest.getUsername());
		AuthResponse authResponse = new AuthResponse();
		authResponse.setAccessToken("Bearer " + jwtToken);
		authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
		authResponse.setUserId(user.getId());
		return authResponse;

	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
		AuthResponse authResponse = new AuthResponse();
		if (userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
			authResponse.setMessage("username var knk zaten");
			return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
		} else {
			User user = new User();
			user.setName(registerRequest.getName());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			user.setRole(registerRequest.getRole());
			user.setSurName(registerRequest.getSurName());
			user.setUserName(registerRequest.getUserName());
			userService.saveOneUser(user);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUserName(), registerRequest.getPassword());
			Authentication auth = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(auth);
			String jwtToken = jwtTokenProvider.generateJwtToken(auth);
			
			authResponse.setMessage("User successfully registered.");
			authResponse.setAccessToken("Bearer " + jwtToken);
			authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
			authResponse.setUserId(user.getId());
			
			return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
		}
	}
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
		AuthResponse response = new AuthResponse();
		RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
		if(token.getToken().equals(refreshRequest.getRefreshToken()) &&
				!refreshTokenService.isRefreshExpired(token)) {

			User user = token.getUser();
			String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
			response.setMessage("token başarılıyla yenilendi.");
			response.setAccessToken("Bearer " + jwtToken);
			response.setUserId(user.getId());
			return new ResponseEntity<>(response, HttpStatus.OK);		
		} else {
			response.setMessage("refresh token geçersiz.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		
	}
}
