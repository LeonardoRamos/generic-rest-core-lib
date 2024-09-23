package com.generic.rest.core.util.encrypter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordEncrypter implements PasswordEncrypter {
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encryptPassword(String passwordToHash) {
		return this.passwordEncoder.encode(passwordToHash);
	}
	
	@Override
	public boolean matchPassword(String password, String hashedPassword) {
		return this.passwordEncoder.matches(password, hashedPassword);
	}
	
}