package com.generic.rest.core.service.auth;

import com.generic.rest.core.domain.AuthEntity;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface with basic Token validation and generation operations.
 * 
 * @author leonardo.ramos
 *
 */
public sealed interface TokenService permits TokenServiceImpl {

	/**
	 * Generate token with given authentication data.
	 * 
	 * @param authEntity
	 * @return Token
	 */
	String generateToken(AuthEntity authEntity);

	/**
	 * Validate given token.
	 * 
	 * @param token
	 * @return If token is valid
	 */
	boolean validateToken(String token);

	/**
	 * Get a claim from within a token.
	 * 
	 * @param token
	 * @param tokenClaim
	 * @return Token claim data.
	 */
	String getTokenClaim(String token, String tokenClaim);

	/**
	 * Get token from http servlet request.
	 * 
	 * @param request
	 * @return Token
	 */
	String getTokenFromRequest(HttpServletRequest request);

	/**
	 * Get token from authorization header data.
	 * 
	 * @param authorizationHeader
	 * @return Token
	 */
	String getTokenFromAuthorizationHeader(String authorizationHeader);

}