package com.generic.rest.core.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.generic.rest.core.BaseConstants.TOMCAT;

/**
 * Tomcat server customizer to allow special characters in embedded server for url request filter.
 * 
 * @author leonardo.ramos
 *
 */
@Configuration
public class TomcatWebServerCustomizer {
	
	/**
	 * Create bean of Server factory configuration for Tomcat embedded server.
	 * @return {@link ConfigurableServletWebServerFactory}.
	 */
	@Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty(TOMCAT.RELAXED_SERVER_CHARS_KEY, TOMCAT.RELAXED_SERVER_CHARS_VALUE);
            connector.setProperty(TOMCAT.RELAXED_SERVER_PATH_KEY, TOMCAT.RELAXED_SERVER_PATH_VALUE);
        });
     
        return factory;
    }
	
}