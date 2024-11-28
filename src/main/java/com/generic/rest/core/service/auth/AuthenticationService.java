package com.generic.rest.core.service.auth;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

/**
 * Interface for credentials authentication in API. 
 * @author leonardo.ramos
 *
 */
@FunctionalInterface
public interface AuthenticationService {

	/**
	 * Attempt authentication in API throught credentials.
	 * @param credentials
	 * @return Authentication token
	 * @throws AuthenticationException
	 */
	public Map<String, String> attemptAuthentication(Map<String, String> credentials) throws AuthenticationException;
	
}