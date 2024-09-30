package com.generic.rest.core.util.encrypter.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.generic.rest.core.util.encrypter.TextEncrypter;

/**
 * implementation of {@link TextEncrypter}.
 * 
 * @author leonardo.ramos
 *
 */
public class BCryptTextEncrypter implements TextEncrypter {
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encryptText(String textToHash) {
		return this.passwordEncoder.encode(textToHash);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matchText(String text, String hashedText) {
		return this.passwordEncoder.matches(text, hashedText);
	}
	
}