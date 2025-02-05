package com.enote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enote.dto.GenericResponse;
import com.enote.service.IUserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	private IUserService userService;

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id){
		Boolean isDeleted = userService.softDelete(id);
		if(isDeleted) {
			return GenericResponse.buildResponse("Status", "User deleted successfully.", null, HttpStatus.OK);
		}else {
			return GenericResponse.buildResponse("Failed", "User deletion failed.", null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
