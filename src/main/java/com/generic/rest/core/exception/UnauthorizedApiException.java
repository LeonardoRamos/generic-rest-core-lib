package com.generic.rest.core.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedApiException extends ApiException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedApiException(String message, String... data) {
        super(HttpStatus.UNAUTHORIZED, message, data);
    }

    public UnauthorizedApiException(String message, Throwable throwable, String... data) {
        super(HttpStatus.UNAUTHORIZED, message, throwable, data);
    }

}