package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for API http status 401 errors.
 * 
 * @author leonardo.ramos
 *
 */
public class UnauthorizedApiException extends ApiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param data
	 */
	public UnauthorizedApiException(String message, String... data) {
        super(HttpStatus.UNAUTHORIZED, message, data);
    }

	/**
     * Constructor
     * 
     * @param message
     * @param throwable
     * @param data
     */
    public UnauthorizedApiException(String message, Throwable throwable, String... data) {
        super(HttpStatus.UNAUTHORIZED, message, throwable, data);
    }

}