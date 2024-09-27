package com.generic.rest.core.service;

import java.util.List;

import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;

public interface ApiRestService<E extends BaseEntity> {
	
	ApiResponse<E> findAll(RequestFilter requestFilter) throws ApiException;
	
	Long countAll(RequestFilter requestFilter) throws ApiException; 
	
	List<E> findAllRecords(RequestFilter requestFilter) throws ApiException;
	
}