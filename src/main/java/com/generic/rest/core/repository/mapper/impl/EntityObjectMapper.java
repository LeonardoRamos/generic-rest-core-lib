package com.generic.rest.core.repository.mapper.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.exception.MapperException;
import com.generic.rest.core.repository.mapper.EntityMapper;
import com.generic.rest.core.util.ReflectionUtils;

import jakarta.persistence.criteria.Selection;

/**
 * Implementation of interface {@link EntityMapper}, responsible for mapping JPA Object result rows
 * that are returned as same type as <E> and are possible for direct type casting.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class EntityObjectMapper<E extends BaseEntity> implements EntityMapper<E> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X extends Object> E mapEntity(Class<E> entityClass, Object row, List<Selection<X>> projection) throws MapperException {
		try {
			
			E entity = (E) row;
			
			if (projection == null || projection.isEmpty()) {
				return entity;
			}
			
			Constructor<?> constructor = entityClass.getConstructor();
			E object = (E) constructor.newInstance();
			
			for (Field field : entityClass.getDeclaredFields()) {
				ReflectionUtils.makeAccessible(field);
				
				if (this.isInProjection(field.getName(), projection)) {
					ReflectionUtils.setField(field, object, field.get(entity));
				}
			}
			
			return object;
			
		} catch (ReflectiveOperationException e) {
			throw new MapperException(e);
		}
	}
	
	/**
	 * Verify if fieldName is list of {@link Selection} projection.
	 * 
	 * @param fieldName
	 * @param projection
	 * @return true if fieldName is in projection false otherwise
	 */
	private <X extends Object> boolean isInProjection(String fieldName, List<Selection<X>> projection) {
		if (projection == null || projection.isEmpty()) {
			return false;
		}
		
		for (Selection<? extends Object> projectionField : projection) {
			if (fieldName.equals(projectionField.getAlias())) {
				return true;
			}
		}
		
		return false;
	}

}
