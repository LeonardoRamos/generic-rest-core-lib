package com.generic.rest.core.repository.mapper;

import java.util.List;

import com.generic.rest.core.domain.BaseEntity;

import jakarta.persistence.criteria.Selection;

/**
 * Interface responsible for mapping entities from the JPA query result into <E> type result.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@FunctionalInterface
public interface ResultMapper<E extends BaseEntity> {
	
	/**
	 * Map result from JPA query.
	 * 
	 * @param entityClass
	 * @param result
	 * @param projection
	 * @return List of mapped entities
	 * @throws ReflectiveOperationException
	 */
	List<E> mapResultSet(Class<E> entityClass, List<Object> result, List<Selection<? extends Object>> projection) throws ReflectiveOperationException;

	

}
