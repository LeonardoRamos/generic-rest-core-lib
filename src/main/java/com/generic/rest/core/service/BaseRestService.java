package com.generic.rest.core.service;

import javax.persistence.EntityNotFoundException;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.BaseRepository;

public abstract class BaseRestService<E extends BaseEntity, R extends BaseRepository<E>> 
	extends ApiRestService<E, R> {
	
	public E findById(Long id) throws NotFoundApiException {
		try {
			return getRepository().getOne(id);
			
		} catch (EntityNotFoundException e) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
	
	@Override
	public E update(E entity) throws ApiException {
		validateExists(entity.getId());
		
		return getRepository().saveAndFlush(entity);
	}
	
	public Boolean delete(Long id) throws ApiException {
		validateExists(id);
		
		getRepository().deleteById(id);
	   
	   	return Boolean.TRUE;
   	}

	private void validateExists(Long id) throws NotFoundApiException {
		Boolean existsEntity = getRepository().existsById(id);
		
		if (Boolean.FALSE.equals(existsEntity)) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
   
	@Override
   	public E save(E entity) throws ApiException {
	   	return getRepository().saveAndFlush(entity);
   	}
	
}