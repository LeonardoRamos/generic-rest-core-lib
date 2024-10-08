package com.generic.rest.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.service.impl.ApiRestServiceImpl;

/**
 * Basic REST Controller with CRUD operations.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 * @param <S>
 */
@SuppressWarnings({ "rawtypes", "unchecked"} )
public abstract class ApiRestController<E extends BaseEntity, S extends ApiRestServiceImpl> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestController.class);
	
	/**
	 * The Rest Service.
	 * 
	 * @return S
	 */
	public abstract S getService();
	
    /**
     * Get all entities with given filter. 
     * 
     * @param requestFilter
     * @return {@link ResponseEntity}
     * @throws ApiException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<E>> getAll(
    		@ModelAttribute("RequestFilter") RequestFilter requestFilter) throws ApiException {
    	LOGGER.info("Finding Entity by requestFilter=[{}]", requestFilter);
		return new ResponseEntity<>(this.getService().findAll(requestFilter), HttpStatus.OK);
    }
    
    /**
     * Insert operation.
     * 
     * @param entity
     * @return {@link ResponseEntity}
     * @throws ApiException
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> insert(@RequestBody E entity) throws ApiException {
    	LOGGER.info("Processing insert of data: [{}]", entity);
		return (ResponseEntity<E>) new ResponseEntity<>(this.getService().save(entity), HttpStatus.CREATED);
    }
    
}