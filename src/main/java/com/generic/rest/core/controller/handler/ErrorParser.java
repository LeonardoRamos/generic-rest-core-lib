package com.generic.rest.core.controller.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.generic.rest.core.BaseConstants;
import com.generic.rest.core.BaseConstants.ERRORKEYS;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.exception.ApiException;

/**
 * Parser class to format and create API error responses based on exception message.
 * 
 * @author leonardo.ramos
 *
 */
public class ErrorParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorParser.class);

	/**
	 * Create {@link ResponseEntity} with list of error and status of given error.
	 * 
	 * @param errors
	 * @param status
	 * @return {@link ResponseEntity}
	 */
	public ResponseEntity<Map<String, Object>> createResponseEntity(List<Map<String, String>> errors, HttpStatus status) {
		return new ResponseEntity<>(Collections.singletonMap(ERRORKEYS.KEY, errors), status);
	}
	
	/**
	 * Format binding errors from entity validation.
	 * 
	 * @param bindingResult
	 * @return formatted list of errors.
	 */
	public List<Map<String, String>> formatErrorList(BindingResult bindingResult) {
		List<Map<String, String>> errors = new ArrayList<>();

		if (bindingResult != null) {
			String message = BaseConstants.MSGERROR.VALIDATION_ERROR;
			String code = this.getErrorCode(message);
					
			for (ObjectError objectError: bindingResult.getAllErrors()) {
				if (objectError.getDefaultMessage() != null) {
					
					if (objectError instanceof FieldError) {
						String fieldError = ((FieldError) objectError).getField();
						
						code = new StringBuilder(code).append("_").append(fieldError).toString().toUpperCase();
					}
					
					message = objectError.getDefaultMessage();
				}
				
				Map<String, String> error = new HashMap<>();
				error.put(ERRORKEYS.ERROR_CODE_KEY, code);
		    	error.put(ERRORKEYS.ERROR_MSG_KEY, message);
				
				errors.add(error);
			}
		}
		
		return errors;
	}
	
	/**
	 * Format erros from {@link ApiException} error messages.
	 * 
	 * @param exception
	 * @return formatted list of errors.
	 */
	public List<Map<String, String>> formatErrorList(ApiException exception) {
    	return this.formatErrorList(exception.getCode(), exception.getData());
	}
	
	/**
	 * Format error message from exception message text.
	 * 
	 * @param message
	 * @return formatted list of errors.
	 */
	public List<Map<String, String>> formatErrorList(String message) {
		Object [] data = null;
		return this.formatErrorList(message, data);
	}
	
	/**
	 * Format error message with given list of error data.
	 * 
	 * @param message
	 * @param data
	 * @return formatted list of errors.
	 */
	public List<Map<String, String>> formatErrorList(String message, Object... data) {
		List<Map<String, String>> errors = new ArrayList<>();
    	
    	errors.add(this.createError(message, data));
		
    	return errors;
	}
	
	/**
	 * Create error map of errors with given list of data. 
	 * 
	 * @param message
	 * @param data
	 * @return formatted list of errors.
	 */
	private Map<String, String> createError(String message, Object... data) {
		Map<String, String> error = new HashMap<>();
    	
		String code = this.getErrorCode(message);

		if (data != null && data.length > 0) {
			StringBuilder dataMessage = new StringBuilder(message).append(" [");
			
			for (int i = 0; i < data.length; i++) {
				dataMessage.append(data[i]);
				
				if (i < (data.length - 1)) {
					dataMessage.append(", ");
				}
			}
			
			message = dataMessage.append("]").toString();
		}
		
		error.put(ERRORKEYS.ERROR_CODE_KEY, code);
    	error.put(ERRORKEYS.ERROR_MSG_KEY, message);
    	
    	return error;
	}
	
	/**
	 * Get respective error code for the error message.
	 * 
	 * @param message
	 * @return error code
	 */
	private String getErrorCode(String message) {
		try {
			Class<?> msgErrorData = BaseConstants.MSGERROR.class;
			
			for (Field errorKeysFields : msgErrorData.getFields()) {
				if (errorKeysFields.get(msgErrorData).equals(message)) {
					return errorKeysFields.getName();
				}
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return MSGERROR.DEFAULT_ERROR_CODE;
	}

}