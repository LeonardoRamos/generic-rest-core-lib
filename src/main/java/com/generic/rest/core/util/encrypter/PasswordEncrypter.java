package com.generic.rest.core.util.encrypter;

public interface PasswordEncrypter {
	
	public String encryptPassword(String passwordToHash);
	
	public boolean matchPassword(String password, String hashedPassword);
	
}