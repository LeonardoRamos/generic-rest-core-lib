package com.generic.rest.core.repository.mapper.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.MapperException;
import com.generic.rest.core.repository.mapper.EntityMapper;

import jakarta.persistence.criteria.Selection;

/**
 * Implementation of interface {@link EntityMapper}, responsible for mapping JPA Object result rows
 * that are returned as an array of Object values representing each field of the entity <E>.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class EntityObjectValuesArrayMapper<E extends BaseEntity> implements EntityMapper<E> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E mapEntity(Class<E> entityClass, Object row, List<Selection<? extends Object>> projection) throws MapperException {
		try {
			Object[] fieldData = (Object[]) row;
			
			Constructor<?> constructor = entityClass.getConstructor();
			E object = (E) constructor.newInstance();
			
			for (int i = 0; i < fieldData.length; i++) {
				this.mapProjectionPath(entityClass, projection, fieldData[i], object, i);
			}
			
			return object;
			
		} catch (ReflectiveOperationException e) {
			throw new MapperException(e);
		}
	}
	
}
