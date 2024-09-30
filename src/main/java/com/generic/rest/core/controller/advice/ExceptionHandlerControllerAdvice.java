
package com.generic.rest.core.controller.advice;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.exception.ApiException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Exception handler advice to handle exception and fomat error response.
 * 
 * @author leonardo.ramos
 *
 */
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

	private ErrorParser errorParser;
	
	/**
	 * Default constructor.
	 */
	public ExceptionHandlerControllerAdvice() {
		this.errorParser = new ErrorParser();
	}
	
	/**
	 * Handles {@link ApiException} request exception errors.
	 * 
	 * @param exception
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Map<String, Object>> apiException(ApiException exception) {
		return this.handler(exception, this.errorParser.formatErrorList(exception), exception.getStatus());
	}
	
	/**
	 * Handles forbidden request exception errors.
	 * 
	 * @param exception
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<Map<String, Object>> forbidden(Exception exception) {
		return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.AUTHENTICATION_FAILED_ERROR), HttpStatus.FORBIDDEN);
	}
	
	/**
	 * Handles generic request exception errors.
	 * 
	 * @param exception
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler({Exception.class })
	public ResponseEntity<Map<String, Object>> exception(Exception exception) {
		return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles bad request exception errors.
	 * 
	 * @param req
	 * @param exception
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler({
		HttpRequestMethodNotSupportedException.class, 
		HttpMediaTypeNotSupportedException.class, 
		HttpMessageNotReadableException.class,
		HttpMediaTypeNotAcceptableException.class,
		MethodArgumentNotValidException.class, 
		MissingServletRequestPartException.class})
	public ResponseEntity<Map<String, Object>> badRequest(HttpServletRequest req, Exception exception) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		if (exception instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
			return this.handler(exception, this.errorParser.formatErrorList(methodArgumentNotValidException.getBindingResult()), status);
		}

		if (exception instanceof HttpMediaTypeNotSupportedException) {
			return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.MEDIA_TYPE_NOT_SUPPORTED), status);
		}
		
		if (exception instanceof HttpRequestMethodNotSupportedException) {
			HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException = (HttpRequestMethodNotSupportedException) exception;
			return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.METHOD_NOT_SUPPORTED, httpRequestMethodNotSupportedException.getMethod()), status);
		
		}
		if (exception instanceof HttpMediaTypeNotAcceptableException) {
			HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException = (HttpMediaTypeNotAcceptableException) exception;
			return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.MEDIA_TYPE_NOT_ACCEPTABLE, httpMediaTypeNotAcceptableException.getSupportedMediaTypes().toString()), status);
		}

		if (exception instanceof MissingServletRequestPartException) {
			MissingServletRequestPartException missingServletRequestPartException = (MissingServletRequestPartException) exception;
			return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.PARAMETER_NOT_PRESENT, missingServletRequestPartException.getRequestPartName()), status);
		}
		
		return handleFormatExceptions(exception, status);
	}

	/**
	 * 
	 * Handles different types of format errors of bad request nature.
	 * 
	 * @param exception
	 * @param status
	 * @return {@link ResponseEntity}
	 */
	private ResponseEntity<Map<String, Object>> handleFormatExceptions(Exception exception, HttpStatus status) {
		if (exception instanceof HttpMessageNotReadableException) {

			if (exception.getCause() instanceof UnrecognizedPropertyException) {
				UnrecognizedPropertyException ex = ((UnrecognizedPropertyException) exception.getCause());
				return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.UNRECOGNIZED_FIELD, ex.getPropertyName()), status);

			} else if (exception.getCause() instanceof InvalidFormatException) {
				StringBuilder path = buildFormatErrorPath(exception);
				return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.INVALID_VALUE, path.toString()), status);

			} else if (exception.getCause() instanceof JsonMappingException) {
				return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.BODY_INVALID), status);

			} else {
				return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.MESSAGE_NOT_READABLE), status);
			}
		}

		return this.handler(exception, this.errorParser.formatErrorList(MSGERROR.BAD_REQUEST_ERROR), status);
	}
	
	/**
	 * Handler for creating and building formatted response.
	 * 
	 * @param exception
	 * @param errors
	 * @param status
	 * @return {@link ResponseEntity}
	 */
	private ResponseEntity<Map<String, Object>> handler(Exception exception, List<Map<String, String>> errors, HttpStatus status) {
		if (status.is5xxServerError()) {
			LOGGER.error(exception.getMessage(), exception);
		} else {
			LOGGER.warn(exception.getMessage(), exception);
		}
		return this.errorParser.createResponseEntity(errors, status);
	}
	
	/**
	 * Build error path format.
	 * 
	 * @param exception
	 * @return formatted error.
	 */
	private StringBuilder buildFormatErrorPath(Exception exception) {
		InvalidFormatException ex = ((InvalidFormatException) exception.getCause());
		StringBuilder path = new StringBuilder();

		for (Reference reference : ex.getPath()) {
			if (!path.toString().equals("")) {
				path.append(".");
			}

			if (reference.getIndex() > - 1) {
				path.append("[").append(reference.getIndex()).append("]");
			} else {
				path.append(reference.getFieldName());
			}
		}
		return path;
	}
    
}