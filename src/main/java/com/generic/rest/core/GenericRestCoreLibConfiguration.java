package com.generic.rest.core;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableCaching
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
public class GenericRestCoreLibConfiguration {
	
}