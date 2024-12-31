package com.enote.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.enote.entity.User;

public interface IJwtService {

	String generateToken(User user);
	
	String extractUserName(String token);
	
	Boolean validateToken(String token, UserDetails userDetails);
}
