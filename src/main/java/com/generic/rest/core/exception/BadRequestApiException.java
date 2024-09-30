package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for API http status 400 errors.
 * 
 * @author leonardo.ramos
 *
 */
public class BadRequestApiException extends ApiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param data
	 */
	public BadRequestApiException(String message, String... data) {
        super(HttpStatus.BAD_REQUEST, message, data);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param throwable
     * @param data
     */
    public BadRequestApiException(String message, Throwable throwable, String... data) {
        super(HttpStatus.BAD_REQUEST, message, throwable, data);
    }

}