package com.generic.rest.core.exception;

/**
 * Exception class for errors while parsing and mapping JPA data results.
 * 
 * @author leonardo.ramos
 *
 */
public class MapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public MapperException() {
        super();
    }

    /**
     * Constructor with message error param.
     * 
     * @param message
     */
    public MapperException(String message) {
        super(message);
    }

    /**
     * Constructor for message error and throwable cause for this exception.
     * 
     * @param message
     * @param cause
     */
    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for throwable cause for this exception.
     * 
     * @param cause
     */
    public MapperException(Throwable cause) {
        super(cause);
    }

}