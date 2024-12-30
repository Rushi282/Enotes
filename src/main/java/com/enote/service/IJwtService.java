package com.enote.service;

import com.enote.entity.User;

public interface IJwtService {

	public String generateToken(User user);
}
