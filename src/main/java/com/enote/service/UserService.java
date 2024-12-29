package com.enote.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enote.config.security.CustomUserDetails;
import com.enote.dao.RoleRepository;
import com.enote.dao.UserRepository;
import com.enote.dto.EmailRequest;
import com.enote.dto.LoginRequest;
import com.enote.dto.LoginResponse;
import com.enote.dto.UserDto;
import com.enote.entity.AccountStatus;
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
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Boolean register(UserDto userDto) throws Exception {
		commonUtil.validateUser(userDto);
		
		User newUser = mapper.map(userDto, User.class);
		
		setRoles(userDto, newUser);
		
		AccountStatus status = AccountStatus.builder().isActive(false)
								.verificationCode(UUID.randomUUID().toString()).build();
		newUser.setStatus(status);
		newUser.setPassword(encoder.encode(newUser.getPassword()));
		User savedUser = userRepo.save(newUser);
		
		if(!ObjectUtils.isEmpty(savedUser)) {
			sendEmail(savedUser);
			return true;
		}
		return false;
	}

	private void sendEmail(User savedUser) throws Exception {
		
		String message = "<h2> Hii, [[username]]</h2><br/>"
						+"Your account created successfully.<br/>"
						+"Click below link to verify your account.<br/>"
						+"<a href='[[url]]'>Click here</a><br/><br/>"
						+"Thanks <b>Enotes.com</b>";
		
		message = message.replace("[[username]]", savedUser.getFirstName());
		message = message.replace("[[url]]", 
				"http://localhost:9292/api/v1/home/verify?uId="+savedUser.getId()+"&code="+savedUser.getStatus().getVerificationCode());
		
		EmailRequest emailRequest = EmailRequest.builder()
										.to(savedUser.getEmail())
										.subject("Account created scuccessfully!!!")
										.title("Account creating confirmation")
										.message(message)
										.build();
		
		emailService.sendMail(emailRequest);
	}

	private void setRoles(UserDto userDto,User newUser) {
		List<Integer> roleIds = userDto.getRoles().stream().map(role -> role.getId()).toList();
		List<Role> roles = roleRepo.findAllById(roleIds);
		newUser.setRoles(roles);
	}

	public LoginResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		if(authenticate.isAuthenticated()) {
			CustomUserDetails customUserDetails = (CustomUserDetails)authenticate.getPrincipal();
			String token = "hgdydxyctygcjhcgjhstrszgfjjf";
			LoginResponse loginResponse = LoginResponse.builder()
											.userDto(mapper
													.map(customUserDetails.getUser(), UserDto.class))
											.token(token)
											.build();
			return loginResponse;
		}
		return null;
	}

}
