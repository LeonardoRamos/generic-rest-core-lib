package com.generic.rest.core.repository.mapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.hibernate.mapping.Set;
import org.hibernate.query.sqm.function.SelfRenderingSqmAggregateFunction;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.query.sqm.tree.expression.SqmDistinct;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.AggregateFunction;
import com.generic.rest.core.exception.MapperException;
import com.generic.rest.core.util.ReflectionUtils;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Selection;

/**
 * Interface responsible for mapping JPA Object result rows fields and aggregations for type <E>.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@FunctionalInterface
public interface EntityMapper<E extends BaseEntity> {

	/**
	 * Map result from JPA query into an actual entity <E>.
	 * 
	 * @param <X>
	 * @param entityClass
	 * @param row
	 * @param projection
	 * @return E
	 * @throws ReflectiveOperationException
	 */
	<X extends Object> E mapEntity(Class<E> entityClass, Object row, List<Selection<X>> projection) throws MapperException;
	
	/**
	 * Map the projection returned from JPA query into entity <E> fields and aggregation fields.
	 * 
	 * @param <X>
	 * @param entityClass
	 * @param projection
	 * @param row
	 * @param object
	 * @param projectionIndex
	 * @throws ReflectiveOperationException
	 */
	default <X extends Object> void mapProjectionPath(Class<E> entityClass, List<Selection<X>>  projection, 
			Object row, E object, int projectionIndex) throws ReflectiveOperationException {
		
		if (projection != null && projection.get(projectionIndex) instanceof SelfRenderingSqmAggregateFunction aggregationFunction) {
			
			SqmBasicValuedSimplePath<Object> attributePath = this.getAggregationPath(aggregationFunction);
			
			if (AggregateFunction.isCountFunction(aggregationFunction.getFunctionName()) ||
					AggregateFunction.isCountDistinctFunction(aggregationFunction.getFunctionName())) {
				object.addCount(this.mapAggregationField(entityClass, row, attributePath));
				
			} else if (AggregateFunction.isSumFunction(aggregationFunction.getFunctionName())) {
				this.mapSumAggregationValues(entityClass, row, object, attributePath);
				
			} else if (AggregateFunction.isAvgFunction(aggregationFunction.getFunctionName())) {
				this.mapAvgAggregationValues(entityClass, row, object, attributePath);
			}
			
		} else if (projection != null) {
			Path<Object> attributePath = (Path<Object>) projection.get(projectionIndex);
			this.mapProjectionField(entityClass, row, attributePath, object);
		}
	}
	
	/**
	 * Return the respective path {@link SqmBasicValuedSimplePath} for a given aggregation. </p>
	 * JPA returns different data structure for count distinct results.
	 * 
	 * @param aggregationFunction
	 * @return {@link SqmBasicValuedSimplePath}
	 */
	private SqmBasicValuedSimplePath<Object> getAggregationPath(SelfRenderingSqmAggregateFunction aggregationFunction) {
		Object aggregationData = aggregationFunction.getArguments().getFirst();

		if (aggregationData instanceof SqmBasicValuedSimplePath) {
			return (SqmBasicValuedSimplePath<Object>) aggregationData;
		} 

		return (SqmBasicValuedSimplePath<Object>) ((SqmDistinct<Object>) aggregationData).getExpression();
	}
	
	/**
	 * Map sum aggregation values.
	 * 
	 * @param entityClass
	 * @param row
	 * @param object
	 * @param attributePath
	 */
	private void mapSumAggregationValues(Class<E> entityClass, Object row, E object, Path<Object> attributePath) {
		if (row.getClass().equals(Double.class)) {
			object.addSum(this.mapAggregationField(entityClass, BigDecimal.valueOf((Double) row), attributePath));
		} else {
			object.addSum(this.mapAggregationField(entityClass, row, attributePath));
		}
	}
	
	/**
	 * Map avg aggregation values.
	 * 
	 * @param entityClass
	 * @param row
	 * @param object
	 * @param attributePath
	 */
	private void mapAvgAggregationValues(Class<E> entityClass, Object row, E object, Path<Object> attributePath) {
		if (row.getClass().equals(Double.class)) {
			object.addAvg(this.mapAggregationField(entityClass, BigDecimal.valueOf((Double) row), attributePath));
		} else {
			object.addAvg(this.mapAggregationField(entityClass, row, attributePath));
		}
	}
	
	/**
	 * Map projection field in case of a query aggregation.
	 * 
	 * @param entityClass
	 * @param fieldData
	 * @param attributePath
	 * @param entity
	 * @throws ReflectiveOperationException
	 */
	private void mapProjectionField(Class<E> entityClass, Object fieldData, Path<Object> attributePath, E entity) 
			throws ReflectiveOperationException {
		
		List<Map<String, Class>> fieldPaths = this.buildNestedFields(entityClass, attributePath);
		
		Integer lastIndex = fieldPaths.size() - 1;
		Map.Entry<String, Class> rootFieldEntry = fieldPaths.get(lastIndex--).entrySet().iterator().next();
		
		if (lastIndex < 0 && fieldPaths.size() == 1) {
			this.setLastProjectionNestedField(entityClass, fieldData, entity, rootFieldEntry);
		
		} else {
			Object rootFieldData = rootFieldEntry.getValue().getConstructor().newInstance();
			Field fieldRoot = ReflectionUtils.getEntityFieldByName(entityClass, rootFieldEntry.getKey());
			ReflectionUtils.makeAccessible(fieldRoot);
			
			Object currentData = rootFieldData;
			
			for (int i = lastIndex; i >= 0; i--) {
				Map.Entry<String, Class> fieldEntry = fieldPaths.get(i).entrySet().iterator().next();
				
				if (i == 0) {
					this.setLastProjectionNestedField(currentData.getClass(), fieldData, currentData, fieldEntry);
					this.setProjectionNestedField(rootFieldData, entity, fieldPaths);
					
				} else {
					Object currentFieldData = fieldEntry.getValue().getConstructor().newInstance();
					Field currentField = ReflectionUtils.getEntityFieldByName(currentData.getClass(), fieldEntry.getKey());
					ReflectionUtils.makeAccessible(currentField);
					
					this.setFieldValue(currentFieldData, currentData, currentField);
				
					currentData = currentFieldData;
				}
			}
		}
	}
	
	/**
	 * Set nested field value for class and subclass.
	 * 
	 * @param rootFieldData
	 * @param entity
	 * @param fieldPaths
	 * @throws ReflectiveOperationException
	 */
	private void setProjectionNestedField(Object rootFieldData, Object entity, List<Map<String, Class>> fieldPaths) 
			throws ReflectiveOperationException {
		
		Object currentObject = entity;
		Object currentProjectionObject = rootFieldData;
		
		for (int i = fieldPaths.size() - 1; i >= 0; i--) {
			Map.Entry<String, Class> fieldEntry = fieldPaths.get(i).entrySet().iterator().next();
			
			Field currentEntityField = ReflectionUtils.getEntityFieldByName(currentObject.getClass(), fieldEntry.getKey());
			ReflectionUtils.makeAccessible(currentEntityField);
			Object currentEntityData = currentEntityField.get(currentObject);
			
			if (currentEntityData == null) {
				this.setFieldValue(currentProjectionObject, currentObject, currentEntityField);
				break;
			
			} else {
				currentObject = currentEntityData;
				
				if ((i - 1) >= 0) {
					Map.Entry<String, Class> projectionEntry = fieldPaths.get(i - 1).entrySet().iterator().next();

					Field currentProjectionField = ReflectionUtils.getEntityFieldByName(currentProjectionObject.getClass(), projectionEntry.getKey());
					ReflectionUtils.makeAccessible(currentProjectionField);
					Object currentProjectionData = currentProjectionField.get(currentProjectionObject);
					
					currentProjectionObject = currentProjectionData;
				}
			}
		}
	}
	
	/**
	 * Map and set the last nested projection of a subfield.
	 * 
	 * @param clazz
	 * @param fieldData
	 * @param object
	 * @param fieldEntry
	 * @throws ReflectiveOperationException
	 */
	private void setLastProjectionNestedField(Class clazz, Object fieldData, Object object, Map.Entry<String, Class> fieldEntry) 
			throws ReflectiveOperationException {
		
		Field fieldRoot = ReflectionUtils.getEntityFieldByName(clazz, fieldEntry.getKey());
		ReflectionUtils.makeAccessible(fieldRoot);
		this.setFieldValue(fieldData, object, fieldRoot);
	}
	
	/**
	 * Build all nested fields from a given {@link Path}.
	 * 
	 * @param entityClass
	 * @param attributePath
	 * @return List of Map that represents a field name and value
	 */
	private List<Map<String, Class>> buildNestedFields(Class<E> entityClass, Path<Object> attributePath) {
		List<Map<String, Class>> fieldPaths = new ArrayList<>();
		
		do {
			if (!entityClass.equals(attributePath.getJavaType())) {
				fieldPaths.add(Collections.singletonMap(attributePath.getAlias(), attributePath.getJavaType()));
			}
			
			attributePath = (Path<Object>) attributePath.getParentPath();
			
		} while (attributePath.getParentPath() != null);
		
		return fieldPaths;
	}
	
	/**
	 * Build a map that represents an aggregation field and value from given {@link Path}.
	 * 
	 * @param entityClass
	 * @param fieldData
	 * @param attributePath
	 * @return Map that represents an aggregation field and value.
	 */
	private Map<String, Object> mapAggregationField(Class<E> entityClass, Object fieldData, Path<Object> attributePath) {
		Map<String, Object> aggregation = new HashMap<>();
		
		do {
			if (!entityClass.equals(attributePath.getJavaType())) {
				aggregation = new HashMap<>();
				aggregation.put(attributePath.getAlias(), fieldData);
				
				fieldData = aggregation;
			}
			
			attributePath = (Path<Object>) attributePath.getParentPath();
		
		} while (attributePath.getParentPath() != null);
		
		return aggregation;
	}
	
	/**
	 * Set a field value in the target entity object.
	 * 
	 * @param fieldDataValue
	 * @param object
	 * @param field
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setFieldValue(Object fieldDataValue, Object object, Field field) 
			throws ReflectiveOperationException {
		
		if (Collection.class.isAssignableFrom(field.getType())) {
			Collection collection = (Collection) field.get(object);
			
			if (collection == null) {
				
				if (Set.class.equals(field.getType())) {
					collection = new HashSet<>();
				
				} else if (Queue.class.equals(field.getType())) {
					collection = new PriorityQueue<>();
				
				} else {
					collection = new ArrayList<>();
				}
			}
			
			collection.add(fieldDataValue);
			ReflectionUtils.setField(field, object, collection);
			
		} else {
			ReflectionUtils.setField(field, object, fieldDataValue);
		}
	}
	
}