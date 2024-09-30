package com.generic.rest.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.generic.rest.core.BaseConstants.CONTROLLER;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.service.impl.BaseRestServiceImpl;

/**
 * Basic REST Controller with CRUD operations for entity ID.
 * 
 * @author leoanrdo.ramos
 *
 * @param <E>
 * @param <S>
 */
@SuppressWarnings({ "rawtypes", "unchecked"} )
public abstract class BaseRestController<E extends BaseEntity, S extends BaseRestServiceImpl> 
	extends ApiRestController<E, S> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestController.class);
	
	/**
	 * Get one operation from entity ID.
	 * 
	 * @param id
	 * @return {@link ResponseEntity}
	 * @throws ApiException
	 */
	@GetMapping(value = CONTROLLER.ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> getOne(@PathVariable(CONTROLLER.ID) Long id) throws ApiException {
    	LOGGER.info("Processing get of entity of id: [{}]", id);
		return (ResponseEntity<E>) new ResponseEntity<>(this.getService().findById(id), HttpStatus.OK);
    }
	
	/**
	 * Update entity operation from ID.
	 * 
	 * @param id
	 * @return {@link ResponseEntity}
	 * @throws ApiException
	 */
    @PutMapping(value = CONTROLLER.ID_PATH, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> update(@PathVariable(CONTROLLER.ID) Long id, 
    		@RequestBody E entity) throws ApiException {
    	LOGGER.info("Processing update of entity of id: [{}]", id);
		return (ResponseEntity<E>) new ResponseEntity<>(this.getService().update(id, entity), HttpStatus.OK);
    }
    
    /**
     * Delete entity from ID.
     * 
     * @param id
     * @return {@link ResponseEntity}
     * @throws ApiException
     */
    @DeleteMapping(value = CONTROLLER.ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> delete(@PathVariable(CONTROLLER.ID) Long id) throws ApiException {
    	LOGGER.info("Processing delete of entity of id: [{}]", id);
    	this.getService().delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
    }
    
}