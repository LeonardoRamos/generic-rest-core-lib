package com.generic.rest.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.generic.rest.core.util.externalid.ExternalIdGenerator;
import com.generic.rest.core.util.externalid.UUIDExternalIdGenerator;

@Service
public abstract class ApiRestService<E extends BaseEntity, R extends BaseRepository<E>> {
	
	@Autowired
	private ApiRepository<E> apiRepository;
	
	protected abstract R getRepository();
	protected abstract Class<E> getEntityClass();
	
	protected ExternalIdGenerator getExternalIdGenerator() {
		return new UUIDExternalIdGenerator();
	}
	
	public ApiResponse<E> findAll(RequestFilter requestFilter) throws ApiException {
		ApiResponse<E> response = new ApiResponse<>();
		List<E> records = findAllRecords(requestFilter);
		response.setRecords(records);
		
		ApiMetadata metadata = new ApiMetadata();
		metadata.setTotalCount(countAll(requestFilter));
		metadata.setPageOffset(requestFilter.getFetchOffset());
		
		if (Boolean.TRUE.equals(requestFilter.hasValidAggregateFunction())) {
			metadata.setPageSize(records.size());
		} else {
			metadata.setPageSize(requestFilter.getFetchLimit());
		}
		
		response.setMetadata(metadata);
		
		return response;
	}
	
	public Long countAll(RequestFilter requestFilter) throws ApiException {
		return apiRepository.countAll(getEntityClass(), requestFilter);
	}
	
	public List<E> findAllRecords(RequestFilter requestFilter) throws ApiException {
		return apiRepository.findAll(getEntityClass(), requestFilter);
	}
	
	public Page<E> findAll(Pageable pageable) {
		return getRepository().findAll(pageable);
	}
	
	public Page<E> findAll(Example<E> example, Pageable pageable) {
		return getRepository().findAll(example, pageable);
	}
	
   	public void deleteInBatch(List<E> entities) {
	   	getRepository().deleteInBatch(entities);
   	}
   	
   	public abstract E update(E entity) throws ApiException;

   	public abstract E save(E entity) throws ApiException;
	
}