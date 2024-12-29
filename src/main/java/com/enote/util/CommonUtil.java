package com.enote.util;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.enote.dao.RoleRepository;
import com.enote.dao.UserRepository;
import com.enote.dto.TodoDto;
import com.enote.dto.TodoDto.TodoPriorityDto;
import com.enote.dto.TodoDto.TodoStatusDto;
import com.enote.dto.UserDto;
import com.enote.enums.TodoPriority;
import com.enote.enums.TodoStatus;
import com.enote.exception.ResourceAlreadyExistException;

@Component
public class CommonUtil {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository userRepo;

	public static String getContentType(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName);
		
		switch(extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
		case "txt":
			return "text/plain";
		case "jpg":
		case "jpeg":
			return "image/jpeg";
		case "png":
			return "image/png";
		case "docx":
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		default:
			return "application/octet-stream";
		}
	}
	
	public static void validateTodoStatus(TodoDto todoDto) {
		TodoStatusDto reqStatus = todoDto.getStatus();
		TodoStatus[] statuses = TodoStatus.values();
		
		Boolean statusFound = false;
		for (TodoStatus todoStatus : statuses) {
			if(todoStatus.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		
		if(!statusFound) {
			throw new IllegalArgumentException("Invalid status !!");
		}
	}
	
	public static void validateTodoPriority(TodoDto todoDto) {
		TodoPriorityDto reqPriority = todoDto.getPriority();
		TodoPriority[] priorities = TodoPriority.values();
		
		Boolean foundPriority = false;
		for(TodoPriority todoPriority : priorities) {
			if(todoPriority.getId().equals(reqPriority.getId())) {
				foundPriority = true;
			}
		}
		
		if(!foundPriority) {
			throw new IllegalArgumentException("Invalid priority !!");
		}
	}
	
	public void validateUser(UserDto userDto) {
		if(!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First name is invalid!");
		}
		
		if(!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last name is invalid!");
		}
		
		if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("email is invalid!");
		}else if(userRepo.existsByEmail(userDto.getEmail())) {
			throw new ResourceAlreadyExistException("Email already used.");
		}
		
		if(!StringUtils.hasText(userDto.getPassword()) || !userDto.getPassword().matches(Constants.PASSWORD_REGEX)) {
			throw new IllegalArgumentException("password is invalid!");
		}
		
		if(!StringUtils.hasText(userDto.getMobileNo()) || !userDto.getMobileNo().matches(Constants.MOBILE_REGEX)) {
			throw new IllegalArgumentException("Mobile number is invalid!");
		}
		
		if(CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role is invalid!");
		}else {
			List<Integer> availRoleIds = roleRepo.findAll().stream().map(role -> role.getId()).toList();
			
			List<Integer> invalidRoleIds = userDto.getRoles().stream()
			.map(role -> role.getId())
			.filter(roleId -> !availRoleIds.contains(roleId)).toList();
			
			if(!CollectionUtils.isEmpty(invalidRoleIds)) {
				throw new IllegalArgumentException("Role is invalid!");
			}
		}
	}
}
