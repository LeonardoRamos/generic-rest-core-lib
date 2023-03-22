package com.generic.rest.core.util.encrypter;

public interface PasswordEncrypter {
	
	public String encryptPassword(String passwordToHash);
	
	public Boolean matchPassword(String password, String hashedPassword);
	
}