package com.generic.rest.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.ApiMetadata;
import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.BadRequestApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.ApiRepository;
import com.generic.rest.core.repository.BaseRepository;
import com.generic.rest.core.service.ApiRestService;

/**
 * Implementation for {@link ApiRestService} to provide basic REST CRUD operations.
 * @author leonardo.ramos
 *
 * @param <E>
 * @param <R>
 */
@Service
public abstract class ApiRestServiceImpl<E extends BaseEntity, R extends BaseRepository<E>> implements ApiRestService<E> {
	
	@Autowired
	private ApiRepository<E> apiRepository;
	
	protected abstract R getRepository();
	protected abstract Class<E> getEntityClass();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(
			cacheNames = "apiEntities", 
			key = "#requestFilter?.getRawRequestFilter()",
			condition = "#requestFilter != null")
	public ApiResponse<E> findAll(RequestFilter requestFilter) throws ApiException {
		ApiResponse<E> response = new ApiResponse<>();
		List<E> records = this.findAllRecords(requestFilter);
		response.setRecords(records);
		
		ApiMetadata metadata = new ApiMetadata();
		metadata.setTotalCount(this.countAll(requestFilter));
		metadata.setPageOffset(requestFilter.getFetchOffset());
		
		if (requestFilter.hasValidAggregateFunction()) {
			metadata.setPageSize(records.size());
		} else {
			metadata.setPageSize(requestFilter.getFetchLimit());
		}
		
		response.setMetadata(metadata);
		
		return response;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long countAll(RequestFilter requestFilter) throws ApiException {
		return this.apiRepository.countAll(this.getEntityClass(), requestFilter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> findAllRecords(RequestFilter requestFilter) throws ApiException {
		return this.apiRepository.findAll(this.getEntityClass(), requestFilter);
	}
	
	/**
	 * Call {@link BaseRepository#findAll}.
	 *
	 * @param pageable.
	 * @return a page of entities
	 */
	public Page<E> findAll(Pageable pageable) {
		return this.getRepository().findAll(pageable);
	}
	
	/**
	 * Call {@link BaseRepository#findAll(Example, Pageable)}.
	 *
	 * @param example.
	 * @param pageable.
	 * @return a {@link Page}.
	 */
	public Page<E> findAll(Example<E> example, Pageable pageable) {
		return this.getRepository().findAll(example, pageable);
	}
	
	/**
	 * Call {@link BaseRepository#deleteAllInBatch(Iterable))}.
	 *
	 * @param entities to be deleted..
	 */
	@Transactional
   	public void deleteInBatch(List<E> entities) {
   		this.getRepository().deleteAllInBatch(entities);
   	}
   	
   	/**
   	 * Update entity.
   	 * 
   	 * @param id
   	 * @param entity
   	 * @return Updated entity.
   	 * @throws ApiException
   	 */
	@Transactional
	public E update(Long id, E entity) throws ApiException {
		if (entity.getId() == null || id == null || !entity.getId().equals(id)) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, id));
		}

		this.validateExists(id);
		
		return this.getRepository().saveAndFlush(entity);
	}
   	
   	/**
	 * Validate if exists an entity for given ID.
	 * 
	 * @param id
	 * @throws NotFoundApiException
	 */
	public void validateExists(Long id) throws NotFoundApiException {
		boolean existsEntity = this.getRepository().existsById(id);
		
		if (!existsEntity) {
			throw new NotFoundApiException(String.format(MSGERROR.BASE_ENTITY_NOT_FOUND_ERROR, id));
		}
	}
	
	/**
	 * Validate if entity is null.
	 * 
	 * @param entity
	 * @throws NotFoundApiException
	 */
	public void validateEntity(E entity) {
		if (entity == null) {
			throw new NotFoundApiException(MSGERROR.ENTITY_NULL_ERROR);
		}
	}
	
   	/**
   	 * Update entity.
   	 * 
   	 * @param id
   	 * @param entity
   	 * @return Updated entity.
   	 * @throws ApiException
   	 */
   	public abstract E update(Object id, E entity) throws ApiException;
   	
   	/**
   	 * Save entity.
   	 * 
   	 * @param entity
   	 * @return Saved entity
   	 * @throws ApiException
   	 */
   	public abstract E save(E entity) throws ApiException;
	
}