package com.enote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.GenericResponse;
import com.enote.dto.UserDto;
import com.enote.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/regi")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception{
		Boolean isRegistered = userService.register(userDto);
		if(isRegistered) {
			return GenericResponse.buildResponse("Success", "User Registered.", null, HttpStatus.CREATED);
		}
		return GenericResponse.buildResponse("Failed", "User not Registered.", null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
