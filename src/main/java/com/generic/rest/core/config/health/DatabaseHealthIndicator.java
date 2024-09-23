package com.generic.rest.core.config.health;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.generic.rest.core.BaseConstants.HEALTHCHECK;

/**
 * Custom health indicator to check DataSource status.
 * @author leonardo.ramos
 *
 */
public class DatabaseHealthIndicator extends AbstractHealthIndicator implements InitializingBean {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Default Constructor
	 */
	public DatabaseHealthIndicator() {
		this(null);
	}

	/**
	 * Constructor
	 * @param dataSource
	 */
	public DatabaseHealthIndicator(DataSource dataSource) {
		super(HEALTHCHECK.DATABASE.FAILED_MESSAGE);
		this.dataSource = dataSource;
		this.jdbcTemplate = (dataSource != null) ? new JdbcTemplate(dataSource) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.dataSource == null) {
			throw new IllegalStateException(HEALTHCHECK.DATABASE.NO_DATASOURCE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (this.dataSource == null) {
			builder.up().withDetail(HEALTHCHECK.DATABASE.KEY, HEALTHCHECK.DATABASE.UNKNOWN_DATASOURCE);
		
		} else {
			this.doDataSourceHealthCheck(builder);
		}
	}

	/**
	 * Check health of dataSource.
	 * @param builder
	 */
	private void doDataSourceHealthCheck(Health.Builder builder) {
		builder.up().withDetail(HEALTHCHECK.DATABASE.KEY, this.getProduct());
		builder.status(this.isConnectionValid() ? Status.UP : Status.DOWN);
	}

	/**
	 * Get Database product identifier name through database template.
	 * @return product name
	 */
	private String getProduct() {
		return this.jdbcTemplate.execute((ConnectionCallback<String>) this::getProduct);
	}

	/**
	 * Get Database product identifier name from connection.
	 * @param connection
	 * @return product name
	 * @throws SQLException
	 */
	private String getProduct(Connection connection) throws SQLException {
		return connection.getMetaData().getDatabaseProductName();
	}

	/**
	 * Get connection status through database template.
	 * @return connection status
	 */
	private boolean isConnectionValid() {
		return this.jdbcTemplate.execute((ConnectionCallback<Boolean>) this::isConnectionValid);
	}

	/**
	 * Get connection status from database connection.
	 * @param connection
	 * @return connection status
	 * @throws SQLException
	 */
	private boolean isConnectionValid(Connection connection) throws SQLException {
		return connection.isValid(0);
	}

	/**
	 * Set dataSource.
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}