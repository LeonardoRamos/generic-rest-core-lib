package com.generic.rest.core.service;

import java.util.List;

import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.util.externalid.ExternalIdGenerator;
import com.generic.rest.core.util.externalid.UUIDExternalIdGenerator;

public interface ApiRestService<E extends BaseEntity> {
	
	default ExternalIdGenerator getExternalIdGenerator() {
		return new UUIDExternalIdGenerator();
	}
	
	ApiResponse<E> findAll(RequestFilter requestFilter) throws ApiException;
	
	Long countAll(RequestFilter requestFilter) throws ApiException; 
	
	List<E> findAllRecords(RequestFilter requestFilter) throws ApiException;
	
}