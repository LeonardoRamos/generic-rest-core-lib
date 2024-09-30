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
import com.generic.rest.core.service.impl.TokenService;

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

	@Value(JWTAUTH.AUTH_DISABLED)
	private boolean authDisabled;
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object objectHandler) {
		if (!this.authDisabled && objectHandler instanceof HandlerMethod) {
			
			HandlerMethod handler = (HandlerMethod) objectHandler;
			
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
	
}