package com.generic.rest.core.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.BaseRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Extends {@link ApiRestServiceImpl} to provide basic REST CRUD operations based on ID and {@link BaseEntity}.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 * @param <R>
 */
public abstract class BaseRestServiceImpl<E extends BaseEntity, R extends BaseRepository<E>> 
	extends ApiRestServiceImpl<E, R> {
	
	/**
	 * Get entity by ID.
	 * 
	 * @param id
	 * @return entity.
	 * @throws NotFoundApiException
	 */
	public E findById(Long id) throws NotFoundApiException {
		try {
			return this.getRepository().getReferenceById(id);
			
		} catch (EntityNotFoundException e) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
	
	/**
	 * Update entity by ID.
	 * 
	 * @param id
	 * @param E
	 * @return Updated entity.
	 * @throws ApiException
	 */
	@Override
	@Transactional
	public E update(Object id, E entity) throws ApiException {
		Long idLong = null;
		
		try {
			idLong = (Long) id;
			
		} catch (Exception e) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
		
		return super.update(idLong, entity);
	}
	
	/**
	 * Delete entity by ID.
	 * 
	 * @param id
	 * @return Number of deleted entities.
	 * @throws ApiException
	 */
	@Transactional
	public boolean delete(Long id) throws ApiException {
		this.validateExists(id);
		
		this.getRepository().deleteById(id);
	   
	   	return true;
   	}

	/**
	 * Save entity based on {@link BaseEntity}.
	 * 
	 * @param E
	 * @return saved entity.
	 * @throws ApiException
	 */
	@Override
	@Transactional
   	public E save(E entity) throws ApiException {
	   	return this.getRepository().saveAndFlush(entity);
   	}
	
}