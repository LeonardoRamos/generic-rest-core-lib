package com.generic.rest.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncrypterUtils {
	
	public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private EncrypterUtils() {
		
	}
	
	public static String encryptPassword(String passwordToHash) {
		return passwordEncoder.encode(passwordToHash);
	}
	
	public static Boolean matchPassword(String password, String hashedPassword) {
		return passwordEncoder.matches(password, hashedPassword);
	}
	
}