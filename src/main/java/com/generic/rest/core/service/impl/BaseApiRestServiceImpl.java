package com.generic.rest.core.service.impl;

import java.util.Calendar;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseApiEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.BadRequestApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.BaseApiRepository;
import com.generic.rest.core.util.external.id.ExternalIdGenerator;
import com.generic.rest.core.util.external.id.impl.UUIDExternalIdGenerator;

/**
 * Extends {@link ApiRestServiceImpl} to provide basic REST CRUD operations based on ExternalId and {@link BaseApiEntity}.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 * @param <R>
 */
public abstract class BaseApiRestServiceImpl<E extends BaseApiEntity, R extends BaseApiRepository<E>> 
	extends ApiRestServiceImpl<E, R>{
	
	/**
	 * Default ExternalId generator.
	 * 
	 * @return {@link ExternalIdGenerator}
	 */
	public ExternalIdGenerator getExternalIdGenerator() {
		return new UUIDExternalIdGenerator();
	}
	
	/**
	 * Get entity by ExternalId.
	 * 
	 * @param externalId
	 * @return entity.
	 * @throws NotFoundApiException
	 */
	public E getByExternalId(String externalId) throws NotFoundApiException {
		E entity = this.getRepository().findOneByExternalId(externalId);
		
		if (entity == null) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITY_NOT_FOUND_ERROR, externalId));
		}
		
		return entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public E update(Object id, E entity) throws ApiException {
		if (entity.getExternalId() == null || id == null || !entity.getExternalId().equals(id.toString())) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, id));
		}

		return this.update(id.toString(), entity);
	}
	
	/**
	 * Update entity by ExternalId.
	 * 
	 * @param externalId
	 * @param E
	 * @return Updated entity.
	 * @throws ApiException
	 */
	public E update(String externalId, E entity) throws ApiException {
		if (entity.getExternalId() == null || externalId == null || !entity.getExternalId().equals(externalId)) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, externalId));
		}

		if (entity.getId() == null) {
			E entityDatabase = this.getByExternalId(externalId);
			entity.setId(entityDatabase.getId());
		}

		entity.setUpdateDate(Calendar.getInstance());
      
		if (entity.isActive()) {
			entity.setDeleteDate(null);
		}
      
		return this.getRepository().saveAndFlush(entity);
	}

	/**
	 * Delete entity by ExternalId.
	 * 
	 * @param externalId
	 * @return Number of deleted entities.
	 * @throws ApiException
	 */
	public Integer delete(String externalId) throws ApiException {
	   	Integer deletedCount = this.getRepository().deleteByExternalId(externalId);
	   
	   	if (deletedCount == 0) {
		   	throw new NotFoundApiException(String.format(MSGERROR.ENTITY_NOT_FOUND_ERROR, externalId));
	   	}
	   
	   	return deletedCount;
	}
    
	/**
	 * Save entity based on {@link BaseApiEntity}.
	 * 
	 * @param E
	 * @return Updated entity.
	 * @throws ApiException
	 */
	@Override
   	public E save(E entity) throws ApiException {
   		if (entity.getExternalId() == null || "".equals(entity.getExternalId())) {
   			entity.setExternalId(getExternalIdGenerator().get());
   		}
   		
   		entity.setInsertDate(Calendar.getInstance());
   		entity.setUpdateDate(entity.getInsertDate());
   		entity.setDeleteDate(null);

	   	return this.getRepository().saveAndFlush(entity);
   	}
	
}