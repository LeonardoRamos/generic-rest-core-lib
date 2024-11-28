package com.generic.rest.core.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.generic.rest.core.BaseConstants.JWTAUTH;

/**
 * Adapter to configure CORS and intercecptors.
 * 
 * @author leonardo.ramos
 *
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
	
	private AuthorizationInterceptor authorizationInterceptor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.authorizationInterceptor)
			.addPathPatterns(JWTAUTH.ALL_PATH_CORS_REGEX);
	}
	
	/**
	 * Create security filter chain for CSRF configuration.
	 * 
	 * @return {@link SecurityFilterChain}.
	 */
	@Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
             return http.csrf(AbstractHttpConfigurer::disable)
            		 	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        		 		.authorizeHttpRequests(auth -> auth
		 				.requestMatchers(JWTAUTH.ALL_PATH_CORS_REGEX).permitAll()).build();
    }
	
	/**
	 * Create CORS configuration bean.
	 * 
	 * @return {@link CorsConfigurationSource}.
	 */
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(JWTAUTH.ALL_PATH_ORIGIN_REGEX));
        configuration.setAllowedMethods(Arrays.asList(
        		HttpMethod.HEAD.name(), 
        		HttpMethod.OPTIONS.name(), 
        		HttpMethod.GET.name(), 
        		HttpMethod.PUT.name(), 
        		HttpMethod.POST.name(), 
        		HttpMethod.DELETE.name(), 
        		HttpMethod.PATCH.name()));
        
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader(JWTAUTH.CONTENT_DISPOSITION);
        configuration.setAllowedHeaders(Arrays.asList(
        		JWTAUTH.AUTHORIZATION, 
        		JWTAUTH.CACHE_CONTROL, 
        		JWTAUTH.CONTENT_TYPE,
        		JWTAUTH.X_ACCESS_TOKEN));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(JWTAUTH.ALL_PATH_CORS_REGEX, configuration);
        
        return source;
    }

	/**
	 * Set the authorizationInterceptor.
	 * 
	 * @param authorizationInterceptor
	 */
	@Autowired
	public void setAuthorizationInterceptor(AuthorizationInterceptor authorizationInterceptor) {
		this.authorizationInterceptor = authorizationInterceptor;
	}
    
	
}