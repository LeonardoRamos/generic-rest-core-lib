package com.generic.rest.core.config;

import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.generic.rest.core.config.health.DatabaseHealthIndicator;

/**
 * Web configuration for datetime parser and Health check indicator for database.
 * 
 * @author leonardo.ramos
 *
 */
@Configuration
public class WebApiConfig {

	/**
	 * Create customizable bean for database health indicator.
	 * @param dataSource
	 * @return {@link HealthIndicator}.
	 */
	@Bean
    public HealthIndicator dbHealthIndicator(DataSource dataSource) {
        return new DatabaseHealthIndicator(dataSource);
    }
	
	/**
	 * Create customizable bean for datatime serialization.
	 * @return {@link Jackson2ObjectMapperBuilderCustomizer}.
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return builder -> builder.timeZone(TimeZone.getDefault());
	}
	
}