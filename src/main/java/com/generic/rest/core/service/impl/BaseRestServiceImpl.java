package com.generic.rest.core.service.impl;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.BaseRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class BaseRestServiceImpl<E extends BaseEntity, R extends BaseRepository<E>> 
	extends ApiRestServiceImpl<E, R> {
	
	public E findById(Long id) throws NotFoundApiException {
		try {
			return this.getRepository().getReferenceById(id);
			
		} catch (EntityNotFoundException e) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
	
	@Override
	public E update(E entity) throws ApiException {
		validateExists(entity.getId());
		
		return this.getRepository().saveAndFlush(entity);
	}
	
	public boolean delete(Long id) throws ApiException {
		validateExists(id);
		
		this.getRepository().deleteById(id);
	   
	   	return true;
   	}

	private void validateExists(Long id) throws NotFoundApiException {
		boolean existsEntity = this.getRepository().existsById(id);
		
		if (!existsEntity) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
   
	@Override
   	public E save(E entity) throws ApiException {
	   	return this.getRepository().saveAndFlush(entity);
   	}
	
}