package com.generic.rest.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.generic.rest.core.BaseConstants.JWTAUTH;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.exception.UnauthorizedApiException;
import com.generic.rest.core.service.auth.TokenServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link HandlerInterceptor} to process JWT Token authenticaton of all endpoints.
 * 
 * @author leonardo.ramos
 *
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

	private boolean authDisabled;
	private TokenServiceImpl tokenService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object objectHandler) {
		if (!this.authDisabled && objectHandler instanceof HandlerMethod handler) {
			
			NoSecurity noSecurity = handler.getMethodAnnotation(NoSecurity.class);
			
			if (noSecurity == null) {
				
				String token = this.tokenService.getTokenFromRequest(request);
				if (token == null || "".equals(token) || !this.tokenService.validateToken(token)) {
					throw new UnauthorizedApiException(MSGERROR.AUTHORIZATION_TOKEN_NOT_VALID);
				}
			}
		}
            
        return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/* Does not need postHandle implementation */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/* Does not need afterCompletion implementation */
	}

	/**
	 * Set the flag authDisabled.
	 * 
	 * @param authDisabled
	 */
	public void setAuthDisabled(@Value(JWTAUTH.AUTH_DISABLED) boolean authDisabled) {
		this.authDisabled = authDisabled;
	}

	/**
	 * Set the tokenService.
	 * 
	 * @param tokenService
	 */
	@Autowired
	public void setTokenService(TokenServiceImpl tokenService) {
		this.tokenService = tokenService;
	}
	
}