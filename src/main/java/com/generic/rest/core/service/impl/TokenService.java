package com.generic.rest.core.service.impl;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.generic.rest.core.BaseConstants.JWTAUTH;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.AuthEntity;
import com.generic.rest.core.util.StringParserUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
@SuppressWarnings("unchecked")
public class TokenService {
	
	private static final Logger log = LoggerFactory.getLogger(TokenService.class);

	@Value(JWTAUTH.EXPIRATION_TIME)
	private Long expirationTime; 
	
	@Value(JWTAUTH.SECRET)
	private String secret;
	
	@Value(JWTAUTH.TOKEN_PREFIX)
	private String tokenPrefix;
	
	public String generateToken(AuthEntity authEntity) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(JWTAUTH.CLAIM_PRINCIPAL_CREDENTIAL, authEntity.getPrincipalCredential());
		claims.put(JWTAUTH.CLAIM_CREDENTIAL_ROLE, authEntity.getCredentialRole());
		claims.put(JWTAUTH.CLAIM_ADDITIONAL_INFO, authEntity.getAdditionalInfo());
		
		return Jwts.builder()
		 		 .subject(authEntity.getAdditionalInfo())
		 		 .issuer(authEntity.getExternalId())
		 		 .claims(claims)
		 		 .expiration(new Date(System.currentTimeMillis() + this.expirationTime))
				 .signWith(this.getSignKey())
				 .compact();
	}
	
	public boolean validateToken(String token) {
		if (token != null) {
			try {
				Jws<Claims> claims = (Jws<Claims>) Jwts.parser()
					.verifyWith(this.getSignKey())
					.build()
					.parse(StringParserUtils.replace(token, this.tokenPrefix, ""));
				
				String issuer = claims.getPayload().getIssuer();
				
				if (StringUtils.isNotBlank(issuer)) {
					return true;
				}
				
			} catch (Exception e) {
				log.error(MSGERROR.AUTH_ERROR_INVALID_TOKEN, token);
				return false;
			}
		}
		
		log.error(MSGERROR.AUTH_ERROR_INVALID_TOKEN, token);
		return false;
	}
	
	public String getTokenClaim(String token, String tokenClaim) {
		if (token != null) {
			try {
				Jws<Claims> claims = (Jws<Claims>) Jwts.parser()
						.verifyWith(this.getSignKey())
						.build()
						.parse(StringParserUtils.replace(token, this.tokenPrefix, ""));
					
				return (String) claims.getPayload().get(tokenClaim);
				
			} catch (Exception e) {
				log.error(MSGERROR.AUTH_ERROR_INVALID_TOKEN, token);
				return null;
			}
		}
		
		log.error(MSGERROR.AUTH_ERROR_INVALID_TOKEN, token);
		return null;
	}
	
	private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getEncoder().encode(this.secret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(JWTAUTH.X_ACCESS_TOKEN);
		if (StringUtils.isNotBlank(token)) {
			return token;
		}
		
		token = (String) request.getAttribute(JWTAUTH.TOKEN);
		if (StringUtils.isNotBlank(token)) {
			return token;
		}
		
		return this.getTokenFromAuthorizationHeader(request.getHeader(JWTAUTH.AUTHORIZATION));
	}
	
	public String getTokenFromAuthorizationHeader(String authorizationHeader) {
		if (authorizationHeader != null && !"".equals(authorizationHeader)) {
			List<String> authorizationHeaderData = StringParserUtils.splitStringList(authorizationHeader, ' ');
			
			if (authorizationHeaderData != null && authorizationHeaderData.size() > 1 && 
					authorizationHeaderData.get(0).equals(JWTAUTH.BEARER)) {
				
				return authorizationHeaderData.get(1);
			}
		}	
		
		return null;
   }

}