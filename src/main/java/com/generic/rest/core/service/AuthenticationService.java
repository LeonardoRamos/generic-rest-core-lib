package com.generic.rest.core.service;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {

	public Map<String, String> attemptAuthentication(Map<String, String> credentials) throws AuthenticationException;
	
}