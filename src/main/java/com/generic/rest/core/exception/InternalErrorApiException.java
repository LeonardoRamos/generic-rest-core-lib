package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for API http status 500 errors.
 * 
 * @author leonardo.ramos
 *
 */
public class InternalErrorApiException extends ApiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param data
	 */
	public InternalErrorApiException(String message, String... data) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }

	/**
     * Constructor
     * 
     * @param message
     * @param throwable
     * @param data
     */
    public InternalErrorApiException(String message, Throwable throwable, String... data) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, throwable, data);
    }

}