package com.generic.rest.core.repository.mapper.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.MapperException;
import com.generic.rest.core.repository.mapper.EntityMapper;

import jakarta.persistence.criteria.Selection;

/**
 * Implementation of interface {@link EntityMapper}, responsible for mapping JPA Object result rows
 * that are returned as an Object value representing an aggregated field of the entity <E>.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class EntityValuesMapper<E extends BaseEntity> implements EntityMapper<E> {

	private static final int ROOT_PROJECTION_FIELD = 0;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E mapEntity(Class<E> entityClass, Object row, List<Selection<? extends Object>> projection) throws MapperException {
		try {
			Constructor<?> constructor = entityClass.getConstructor();
			E object = (E) constructor.newInstance();
			
			this.mapProjectionPath(entityClass, projection, row, object, ROOT_PROJECTION_FIELD);
			
			return object;
			
		} catch (ReflectiveOperationException e) {
			throw new MapperException(e);
		} 
		
	}

}
