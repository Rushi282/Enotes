package com.enote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.GenericResponse;
import com.enote.service.HomeService;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyAccount(@RequestParam Integer uId, @RequestParam String code){
		Boolean isVerified = homeService.verifyUserAccount(uId, code);
		if(isVerified) {
			return GenericResponse.buildResponse("Status", "Account activated successfully!!", null, HttpStatus.OK);
		}
		return GenericResponse.buildResponse("Failed", "Invalid verfication link", null, HttpStatus.BAD_REQUEST);
	}
}
