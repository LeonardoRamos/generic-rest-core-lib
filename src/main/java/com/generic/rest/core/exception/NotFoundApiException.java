package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for API http status 404 errors.
 * 
 * @author leonardo.ramos
 *
 */
public class NotFoundApiException extends ApiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param data
	 */
	public NotFoundApiException(String message, String... data) {
        super(HttpStatus.NOT_FOUND, message, data);
    }

	/**
     * Constructor
     * 
     * @param message
     * @param throwable
     * @param data
     */
    public NotFoundApiException(String message, Throwable throwable, String... data) {
        super(HttpStatus.NOT_FOUND, message, throwable, data);
    }

}