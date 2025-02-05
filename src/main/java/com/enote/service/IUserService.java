package com.enote.service;

import com.enote.dto.LoginRequest;
import com.enote.dto.LoginResponse;
import com.enote.dto.UserDto;

public interface IUserService {
	Boolean register(UserDto userDto) throws Exception;
	
	LoginResponse login(LoginRequest loginRequest);
	
	Boolean softDelete(Integer id);
}
