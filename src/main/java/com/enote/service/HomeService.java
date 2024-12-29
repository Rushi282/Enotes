package com.enote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enote.dao.UserRepository;
import com.enote.entity.AccountStatus;
import com.enote.entity.User;
import com.enote.exception.ResourceNotFoundException;
import com.enote.exception.SuccessException;

@Service
public class HomeService implements IHomeService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public Boolean verifyUserAccount(Integer uId, String code) {
		User foundUser = userRepo.findById(uId)
		.orElseThrow(() -> new ResourceNotFoundException("User not found of id: "+uId));
		if(foundUser.getStatus().getVerificationCode() == null) {
			throw new SuccessException("Account already verified!!");
		}
		
		if(foundUser.getStatus().getVerificationCode().equals(code)) {
			AccountStatus status = foundUser.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			
			userRepo.save(foundUser);
			
			return true;
		}
		return false;
	}

}
