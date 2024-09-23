package com.generic.rest.core.repository.mapper;

import java.lang.reflect.Constructor;
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

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import org.hibernate.mapping.Set;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;

import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.AggregateFunction;
import com.generic.rest.core.util.ReflectionUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApiResultMapper<E extends BaseEntity> {
	
	public List<E> mapResultSet(
			Class<E> entityClass, 
			List<Object> result, 
			List<Selection<? extends Object>> projection) throws ReflectiveOperationException {
		
		List<E> entities = new ArrayList<>();
		
		for (Object row : result) {
			
			if (row != null) {
				if (Object[].class.equals(row.getClass())) {
					entities.add(this.mapSimpleValuesSelection(entityClass, projection, row));
					
				} else if (entityClass.equals(row.getClass())) {
					entities.add(this.mapEntityObject(entityClass, projection, row));
					
				} else {
					entities.add(this.mapEntityValues(entityClass, projection, row));
				}
			}
		}
		
		return entities;
	}
	
	private E mapSimpleValuesSelection(
			Class<E> entityClass, 
			List<Selection<? extends Object>> projection, 
			Object row) throws ReflectiveOperationException {
		
		Object[] fieldData = (Object[]) row;
		
		Constructor<?> constructor = entityClass.getConstructor();
		E object = (E) constructor.newInstance();
		
		for (int i = 0; i < fieldData.length; i++) {
		
			if (projection != null && projection.get(i) instanceof AggregationFunction) {
				AggregationFunction aggregationFunction = (AggregationFunction) projection.get(i);
				Path<Object> attributePath = (Path<Object>) aggregationFunction.getArgumentExpressions().get(0);
				
				if (AggregateFunction.isCountFunction(aggregationFunction.getFunctionName())) {
					object.addCount(this.mapAggregationField(entityClass, fieldData[i], attributePath));
					
				} else if (AggregateFunction.isSumFunction(aggregationFunction.getFunctionName())) {
					this.mapSumAggregationValues(entityClass, fieldData[i], object, attributePath);
					
				} else if (AggregateFunction.isAvgFunction(aggregationFunction.getFunctionName())) {
					this.mapAvgAggregationValues(entityClass, fieldData[i], object, attributePath);
				}
				
			} else if (projection != null) {
				Path<Object> attributePath = (Path<Object>) projection.get(i);
				this.mapProjectionField(entityClass, fieldData[i], attributePath, object);
			}
		}
		
		return object;
	}

	private E mapEntityObject(
			Class<E> entityClass, 
			List<Selection<? extends Object>> projection,
			Object row) throws ReflectiveOperationException {
		
		E entity = (E) row;
		
		if (projection == null || projection.isEmpty()) {
			return entity;
		}
		
		Constructor<?> constructor = entityClass.getConstructor();
		E object = (E) constructor.newInstance();
		
		for (Field field : entityClass.getDeclaredFields()) {
		    field.setAccessible(true);
		    
			if (this.isInProjection(field.getName(), projection)) {
				field.set(object, field.get(entity));
			}
		}
		
		return object;
	}
	
	private boolean isInProjection(String fieldName, List<Selection<? extends Object>> projection) {
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

	private E mapEntityValues(
			Class<E> entityClass, 
			List<Selection<? extends Object>> projection,
			Object row) throws ReflectiveOperationException {
		
		Constructor<?> constructor = entityClass.getConstructor();
		E object = (E) constructor.newInstance();
		
		if (projection != null &&projection.get(0) instanceof AggregationFunction) {
			AggregationFunction aggregationFunction = (AggregationFunction) projection.get(0);
			Path<Object> attributePath = (Path<Object>) aggregationFunction.getArgumentExpressions().get(0);
			
			if (AggregateFunction.isCountFunction(aggregationFunction.getFunctionName()) ||
					AggregateFunction.isCountDistinctFunction(aggregationFunction.getFunctionName())) {
				object.addCount(this.mapAggregationField(entityClass, row, attributePath));
				
			} else if (AggregateFunction.isSumFunction(aggregationFunction.getFunctionName())) {
				this.mapSumAggregationValues(entityClass, row, object, attributePath);
				
			} else if (AggregateFunction.isAvgFunction(aggregationFunction.getFunctionName())) {
				this.mapAvgAggregationValues(entityClass, row, object, attributePath);
			}
			
		} else if (projection != null) {
			Path<Object> attributePath = (Path<Object>) projection.get(0);
			this.mapProjectionField(entityClass, row, attributePath, object);
		}
		
		return object;
	}

	private void mapSumAggregationValues(Class<E> entityClass, Object row, E object, Path<Object> attributePath) {
		if (row.getClass().equals(Double.class)) {
			object.addSum(this.mapAggregationField(entityClass, BigDecimal.valueOf((Double) row), attributePath));
		} else {
			object.addSum(this.mapAggregationField(entityClass, row, attributePath));
		}
	}
	
	private void mapAvgAggregationValues(Class<E> entityClass, Object row, E object, Path<Object> attributePath) {
		if (row.getClass().equals(Double.class)) {
			object.addAvg(this.mapAggregationField(entityClass, BigDecimal.valueOf((Double) row), attributePath));
		} else {
			object.addAvg(this.mapAggregationField(entityClass, row, attributePath));
		}
	}
	
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
			fieldRoot.setAccessible(true);
			
			Object currentData = rootFieldData;
			
			for (int i = lastIndex; i >= 0; i--) {
				Map.Entry<String, Class> fieldEntry = fieldPaths.get(i).entrySet().iterator().next();
				
				if (i == 0) {
					this.setLastProjectionNestedField(currentData.getClass(), fieldData, currentData, fieldEntry);
					this.setProjectionNestedField(rootFieldData, entity, fieldPaths);
					
				} else {
					Object currentFieldData = fieldEntry.getValue().getConstructor().newInstance();
					Field currentField = ReflectionUtils.getEntityFieldByName(currentData.getClass(), fieldEntry.getKey());
					currentField.setAccessible(true);
					
					this.setFieldValue(currentFieldData, currentData, currentField);
				
					currentData = currentFieldData;
				}
			}
		}
	}
	
	private void setProjectionNestedField(Object rootFieldData, Object entity, List<Map<String, Class>> fieldPaths) 
			throws ReflectiveOperationException {
		
		Object currentObject = entity;
		Object currentProjectionObject = rootFieldData;
		
		for (int i = fieldPaths.size() - 1; i >= 0; i--) {
			Map.Entry<String, Class> fieldEntry = fieldPaths.get(i).entrySet().iterator().next();
			
			Field currentEntityField = ReflectionUtils.getEntityFieldByName(currentObject.getClass(), fieldEntry.getKey());
			currentEntityField.setAccessible(true);
			Object currentEntityData = currentEntityField.get(currentObject);
			
			if (currentEntityData == null) {
				this.setFieldValue(currentProjectionObject, currentObject, currentEntityField);
				break;
			
			} else {
				currentObject = currentEntityData;
				
				if ((i - 1) >= 0) {
					Map.Entry<String, Class> projectionEntry = fieldPaths.get(i - 1).entrySet().iterator().next();

					Field currentProjectionField = ReflectionUtils.getEntityFieldByName(currentProjectionObject.getClass(), projectionEntry.getKey());
					currentProjectionField.setAccessible(true);
					Object currentProjectionData = currentProjectionField.get(currentProjectionObject);
					
					currentProjectionObject = currentProjectionData;
				}
			}
		}
	}

	private void setLastProjectionNestedField(Class clazz, Object fieldData, Object object, Map.Entry<String, Class> fieldEntry) 
			throws ReflectiveOperationException {
		
		Field fieldRoot = ReflectionUtils.getEntityFieldByName(clazz, fieldEntry.getKey());
		fieldRoot.setAccessible(true);
		this.setFieldValue(fieldData, object, fieldRoot);
	}

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

	private void setFieldValue(Object fieldDataValue, Object object, Field field) 
			throws IllegalArgumentException, IllegalAccessException {
		
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
			field.set(object, collection);
			
		} else {
			field.set(object, fieldDataValue);
		}
	}
	
}