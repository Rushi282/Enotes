package com.enote.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String mobileNo;
	
	private List<RoleDto> roles;
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class RoleDto{
		private Integer id;
		
		private String name;
	}
}
