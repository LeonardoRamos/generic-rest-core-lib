package com.generic.rest.core.service;

import java.util.List;

import com.generic.rest.core.domain.ApiResponse;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.ApiException;

/**
 * Interface for basic REST queries based on {@link RequestFilter}.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
public interface ApiRestService<E extends BaseEntity> {
	
	/**
	 * Query data according to {@link RequestFilter} api filters.
	 * 
	 * @param requestFilter
	 * @return API Response with data records and count metadata.
	 * @throws ApiException
	 */
	ApiResponse<E> findAll(RequestFilter requestFilter) throws ApiException;
	
	/**
	 * Count the query data according to {@link RequestFilter} api filters.
	 * 
	 * @param requestFilter
	 * @return Data count.
	 * @throws ApiException
	 */
	Long countAll(RequestFilter requestFilter) throws ApiException; 
	
	/**
	 * Search the query data records according to {@link RequestFilter} api filters.
	 * 
	 * @param requestFilter
	 * @return Records for data.
	 * @throws ApiException
	 */
	List<E> findAllRecords(RequestFilter requestFilter) throws ApiException;
	
}