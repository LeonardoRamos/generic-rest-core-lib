package com.generic.rest.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.generic.rest.core.domain.ApiMetadata;
import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.repository.ApiRepository;
import com.generic.rest.core.repository.BaseRepository;
import com.generic.rest.core.service.ApiRestService;

@Service
public abstract class ApiRestServiceImpl<E extends BaseEntity, R extends BaseRepository<E>> implements ApiRestService<E> {
	
	@Autowired
	private ApiRepository<E> apiRepository;
	
	protected abstract R getRepository();
	protected abstract Class<E> getEntityClass();
	
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
	
	@Override
	public Long countAll(RequestFilter requestFilter) throws ApiException {
		return this.apiRepository.countAll(this.getEntityClass(), requestFilter);
	}
	
	@Override
	public List<E> findAllRecords(RequestFilter requestFilter) throws ApiException {
		return this.apiRepository.findAll(this.getEntityClass(), requestFilter);
	}
	
	public Page<E> findAll(Pageable pageable) {
		return this.getRepository().findAll(pageable);
	}
	
	public Page<E> findAll(Example<E> example, Pageable pageable) {
		return this.getRepository().findAll(example, pageable);
	}
	
   	public void deleteInBatch(List<E> entities) {
   		this.getRepository().deleteAllInBatch(entities);
   	}
   	
   	public abstract E update(E entity) throws ApiException;

   	public abstract E save(E entity) throws ApiException;
	
}