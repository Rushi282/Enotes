package com.enote.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enote.dao.RoleRepository;
import com.enote.dao.UserRepository;
import com.enote.dto.UserDto;
import com.enote.entity.Role;
import com.enote.entity.User;
import com.enote.util.CommonUtil;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean register(UserDto userDto) {
		commonUtil.validateUser(userDto);
		
		User newUser = mapper.map(userDto, User.class);
		
		setRoles(userDto, newUser);
		
		User savedUser = userRepo.save(newUser);
		
		if(!ObjectUtils.isEmpty(savedUser)) {
			return true;
		}
		return false;
	}

	private void setRoles(UserDto userDto,User newUser) {
		List<Integer> roleIds = userDto.getRoles().stream().map(role -> role.getId()).toList();
		List<Role> roles = roleRepo.findAllById(roleIds);
		newUser.setRoles(roles);
	}

}
