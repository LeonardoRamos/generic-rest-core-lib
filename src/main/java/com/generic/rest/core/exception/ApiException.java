package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for API http errors.
 * 
 * @author leonardo.ramos
 *
 */
public abstract class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String code;
	private final transient Object[] data;
	private final HttpStatus status;

	/**
	 * Constructor.
	 * 
	 * @param status
	 * @param code
	 * @param data
	 */
	protected ApiException(HttpStatus status, String code, String... data) {
		this(status, code, null, data);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param status
	 * @param message
	 * @param throwable
	 * @param data
	 */
	protected ApiException(HttpStatus status, String message, Throwable throwable, String... data) {
		super(message, throwable);
		this.code = message;
		this.status = status;
		this.data = data;
	}
	
	/**
	 * Return the code.
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Return error data.
	 * 
	 * @return error data
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Return the http status error.
	 * 
	 * @return {@link HttpStatus}
	 */
	public HttpStatus getStatus() {
		return status;
	}
	
}