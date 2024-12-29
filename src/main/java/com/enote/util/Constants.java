package com.enote.util;

public class Constants {

	public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
//	Minimum eight characters, at least one uppercase letter, 
//	one lowercase letter, one number and one special character:
	public static final String PASSWORD_REGEX ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	public static final String MOBILE_REGEX = "^[6-9][0-9]{9}";
}
