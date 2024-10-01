package com.generic.rest.core.repository.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.repository.mapper.EntityMapper;
import com.generic.rest.core.repository.mapper.ResultMapper;

import jakarta.persistence.criteria.Selection;

/**
 * Implementation of interface {@link ResultMapper} responsible for mapping entities from the JPA query result into <E> type result
 * according to the respective query result type.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
public class ApiResultMapper<E extends BaseEntity> implements ResultMapper<E> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> mapResultSet(
			Class<E> entityClass, 
			List<Object> result, 
			List<Selection<? extends Object>> projection) throws ReflectiveOperationException {
		
		List<E> entities = new ArrayList<>();
		
		for (Object row : result) {
			
			if (row != null) {
				EntityMapper<E> mapper;
				
				if (Object[].class.equals(row.getClass())) {
					mapper = new EntityObjectValuesArrayMapper<>();
					
				} else if (entityClass.equals(row.getClass())) {
					mapper = new EntityObjectMapper<>();
					
				} else {
					mapper = new EntityValuesMapper<>();
				}
				
				entities.add(mapper.mapEntity(entityClass, row, projection));
			}
		}
		
		return entities;
	}
	
}