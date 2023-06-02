package com.generic.rest.core.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.generic.rest.core.BaseConstants.JWTAUTH;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.exception.UnauthorizedApiException;
import com.generic.rest.core.service.TokenService;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

	@Value(JWTAUTH.AUTH_DISABLED)
	private Boolean authDisabled;
	
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object objectHandler) {
		if ((authDisabled == null || Boolean.FALSE.equals(authDisabled)) &&
				objectHandler instanceof HandlerMethod) {
			
			HandlerMethod handler = (HandlerMethod) objectHandler;
			
			NoSecurity noSecurity = handler.getMethodAnnotation(NoSecurity.class);
			if (noSecurity == null) {
				
				String token = tokenService.getTokenFromRequest(request);
				if (token == null || "".equals(token) || Boolean.FALSE.equals(tokenService.validateToken(token))) {
					throw new UnauthorizedApiException(MSGERROR.AUTHORIZATION_TOKEN_NOT_VALID);
				}
			}
		}
            
        return Boolean.TRUE;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/* Does not need postHandle implementation */
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/* Does not need afterCompletion implementation */
	}
	
}