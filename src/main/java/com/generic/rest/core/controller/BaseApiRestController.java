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
import com.generic.rest.core.domain.BaseApiEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.service.impl.BaseApiRestServiceImpl;

/**
 * Basic REST Controller with CRUD operations for entity ExternalId.
 * 
 * @author leoanrdo.ramos
 *
 * @param <E>
 * @param <S>
 */
@SuppressWarnings({ "rawtypes", "unchecked"} )
public abstract class BaseApiRestController<E extends BaseApiEntity, S extends BaseApiRestServiceImpl>
	extends ApiRestController<E, S> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseApiRestController.class);
	
	/**
	 * Get one operation from entity ExternalId.
	 * 
	 * @param id
	 * @return {@link ResponseEntity}
	 * @throws ApiException
	 */
	@GetMapping(value = CONTROLLER.EXTERNAL_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> getOne(@PathVariable(CONTROLLER.EXTERNAL_ID) String externalId) throws ApiException {
    	LOGGER.info("Processing get of entity of externalId: [{}]", externalId);
		return (ResponseEntity<E>) new ResponseEntity<>(this.getService().getByExternalId(externalId), HttpStatus.OK);
    }
	
	/**
	 * Update entity operation from ExternalId.
	 * 
	 * @param externalId
	 * @return {@link ResponseEntity}
	 * @throws ApiException
	 */
    @PutMapping(value = CONTROLLER.EXTERNAL_ID_PATH, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> update(@PathVariable(CONTROLLER.EXTERNAL_ID) String externalId, 
    		@RequestBody E entity) throws ApiException {
    	LOGGER.info("Processing update of entity of externalId: [{}]", externalId);
		return (ResponseEntity<E>) new ResponseEntity<>(this.getService().update(externalId, entity), HttpStatus.OK);
    }
    
    /**
     * Delete entity from ExternalId.
     * 
     * @param externalId
     * @return {@link ResponseEntity}
     * @throws ApiException
     */
    @DeleteMapping(value = CONTROLLER.EXTERNAL_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> delete(@PathVariable(CONTROLLER.EXTERNAL_ID) String externalId) throws ApiException {
    	LOGGER.info("Processing delete of entity of externalId: [{}]", externalId);
    	this.getService().delete(externalId);
		return new ResponseEntity<>(HttpStatus.OK);
    }
    
}