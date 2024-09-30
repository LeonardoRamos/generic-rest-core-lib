package com.generic.rest.core.repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.generic.rest.core.BaseConstants;
import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.filter.AggregateFunction;
import com.generic.rest.core.domain.filter.FilterExpression;
import com.generic.rest.core.domain.filter.FilterField;
import com.generic.rest.core.domain.filter.FilterOperator;
import com.generic.rest.core.domain.filter.FilterOrder;
import com.generic.rest.core.domain.filter.LogicOperator;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.BadRequestApiException;
import com.generic.rest.core.util.ReflectionUtils;
import com.generic.rest.core.util.StringParserUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

/**
 * Class responsible for building JPA query related filters.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@SuppressWarnings({ "unchecked", "rawtypes" } )
public class ApiQueryBuilder<E> {
	
	/**
	 * Verify if given projection has any collection / multi valued field.
	 * 
	 * @param projection
	 * @return true if selection has multi valued / collection projection, false otherwise.
	 */
	public boolean containsMultiValuedProjection(List<Selection<? extends Object>> projection) {
		if (projection == null || projection.isEmpty()) {
			return false;
		}
		
		for (Selection<? extends Object> projectionField : projection) {
			Path<Object> attributePath = (Path<Object>) projectionField;
			
			if (Collection.class.isAssignableFrom(attributePath.getJavaType())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Build group by fields selection from {@link RequestFilter}.
	 * 
	 * @param requestFilter
	 * @param root
	 * @param entityClass
	 * @return Selection of group by fields
	 * @throws BadRequestApiException
	 */
	public List<Selection<? extends Object>> getGroupByFields(RequestFilter requestFilter, Root<?> root, Class<E> entityClass) throws BadRequestApiException {
		try {
			List<String> groupByFields = StringParserUtils.splitStringList(requestFilter.getGroupBy(), ',');
			return this.buildProjectionSelection(root, entityClass, groupByFields);
			
		} catch (Exception e) {
			throw new BadRequestApiException(String.format(MSGERROR.PARSE_PROJECTIONS_ERROR, requestFilter.getProjection()), e);
		}
	}
	
	/**
	 * Build projection fields selection from {@link RequestFilter}.
	 * 
	 * @param requestFilter
	 * @param root
	 * @param entityClass
	 * @return Selection of projection fields
	 * @throws BadRequestApiException
	 */
	public List<Selection<? extends Object>> getProjectionFields(RequestFilter requestFilter, Root<?> root, Class<E> entityClass) throws BadRequestApiException {
		try {
			List<String> projectionFields = StringParserUtils.splitStringList(requestFilter.getProjection(), ',');
			return this.buildProjectionSelection(root, entityClass, projectionFields);
			
		} catch (Exception e) {
			throw new BadRequestApiException(String.format(MSGERROR.PARSE_PROJECTIONS_ERROR, requestFilter.getProjection()), e);
		}
	}
	
	/**
	 * Build list of selection of a given projection by its real classes types.
	 * 
	 * @param root
	 * @param entityClass
	 * @param projectionFields
	 * @return Selection of group by fields
	 * @throws NoSuchFieldException
	 */
	private List<Selection<? extends Object>> buildProjectionSelection(Root<?> root, Class<E> entityClass,
			List<String> projectionFields) throws NoSuchFieldException {
		
		List<Selection<? extends Object>> projection = new ArrayList<>();
		
		if (!projectionFields.isEmpty()) {
			
			for (String fieldName : projectionFields) {
				List<Field> fields = splitFields(entityClass, fieldName);
				projection.add(this.getFieldExpressionPath(fields, root));
			}
		}
		
		return projection;
	}
	
	/**
	 * Build aggregation fields selection from {@link RequestFilter}.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param entityClass
	 * @param requestFilter
	 * @return Selection of aggregation fields
	 * @throws BadRequestApiException
	 */
	public List<Selection<? extends Object>> getAggregateSelection(Root<?> root, CriteriaBuilder criteriaBuilder, Class<E> entityClass,
			RequestFilter requestFilter) throws BadRequestApiException {
		try {
			List<String> sumFields = StringParserUtils.splitStringList(requestFilter.getSum(), ',');
			List<String> avgFields = StringParserUtils.splitStringList(requestFilter.getAvg(), ',');
			List<String> countFields = StringParserUtils.splitStringList(requestFilter.getCount(), ',');
			List<String> countDistinctFields = StringParserUtils.splitStringList(requestFilter.getCountDistinct(), ',');
			List<String> groupByFields = StringParserUtils.splitStringList(requestFilter.getGroupBy(), ',');
			
			List<Selection<? extends Object>> aggregationFields = new ArrayList<>();
			
			if (!sumFields.isEmpty()) {
				this.addAggregationFields(root, criteriaBuilder, entityClass, sumFields, aggregationFields, AggregateFunction.SUM.name());
			}
			
			if (!countFields.isEmpty()) {
				this.addAggregationFields(root, criteriaBuilder, entityClass, countFields, aggregationFields, AggregateFunction.COUNT.name());
			}
			
			if (!countDistinctFields.isEmpty()) {
				this.addAggregationFields(root, criteriaBuilder, entityClass, countDistinctFields, aggregationFields, AggregateFunction.COUNT_DISTINCT.name());
			}
			
			if (!avgFields.isEmpty()) {
				this.addAggregationFields(root, criteriaBuilder, entityClass, avgFields, aggregationFields, AggregateFunction.AVG.name());
			}
			
			if (!groupByFields.isEmpty()) {
				this.addAggregationFields(root, criteriaBuilder, entityClass, groupByFields, aggregationFields, null);
			}
			
			return aggregationFields;
			
		} catch (Exception e) {
			throw new BadRequestApiException(String.format(MSGERROR.PARSE_PROJECTIONS_ERROR, requestFilter.getProjection()), e);
		}
	}

	/**
	 * Add aggregation filter path to an existing JPA {@link CriteriaBuilder}.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param entityClass
	 * @param requestFields
	 * @param aggregationFields
	 * @param aggregateFunction
	 * @throws NoSuchFieldException
	 */
	private void addAggregationFields(Root<?> root, CriteriaBuilder criteriaBuilder, Class<E> entityClass,
			List<String> requestFields, List<Selection<? extends Object>> aggregationFields, String aggregateFunction) throws NoSuchFieldException {
		
		for (String fieldName : requestFields) {
			List<Field> fields = splitFields(entityClass, fieldName);
			
			if (AggregateFunction.isSumFunction(aggregateFunction)) {
				aggregationFields.add(criteriaBuilder.sum(this.getFieldExpressionPath(fields, root)));
				
			} else if (AggregateFunction.isAvgFunction(aggregateFunction)) {
				aggregationFields.add(criteriaBuilder.avg(this.getFieldExpressionPath(fields, root)));
				
			} else if (AggregateFunction.isCountFunction(aggregateFunction)) {
				aggregationFields.add(criteriaBuilder.count(this.getFieldExpressionPath(fields, root)));
				
			} else if (AggregateFunction.isCountDistinctFunction(aggregateFunction)) {
				aggregationFields.add(criteriaBuilder.countDistinct(this.getFieldExpressionPath(fields, root)));
				
			} else {
				aggregationFields.add(this.getFieldExpressionPath(fields, root));
			}
		}
	}

	/**
	 * Build a list of {@link Predicate} according to a query filter of a given {@link RequestFilter}.
	 * 
	 * @param entityClass
	 * @param requestFilter
	 * @param criteriaBuilder
	 * @param root
	 * @return List of {@link Predicate}
	 * @throws BadRequestApiException
	 */
	public List<Predicate> getRestrictions(
			Class<E> entityClass,
			RequestFilter requestFilter, 
			CriteriaBuilder criteriaBuilder,
			Root<?> root) throws BadRequestApiException {
		try {
			FilterExpression currentExpression = FilterExpression.of(requestFilter.getFilter());
			List<Predicate> restrictions = new ArrayList<>();
			
			while (currentExpression != null) {
				List<Predicate> conjunctionRestrictions = new ArrayList<>();
				
				if (currentExpression.getFilterField() != null) {
					if (LogicOperator.OR.equals(currentExpression.getLogicOperator())) {
						
						currentExpression = this.getOrRestrictions(entityClass, criteriaBuilder, root, currentExpression, conjunctionRestrictions);
						
						List<Predicate> orParsedRestrictions = new ArrayList<>();
						orParsedRestrictions.add(criteriaBuilder.or(conjunctionRestrictions.toArray(new Predicate[]{})));
					
						restrictions.add(criteriaBuilder.and(orParsedRestrictions.toArray(new Predicate[]{})));

					} else {
						conjunctionRestrictions.add(buildPredicate(entityClass, currentExpression.getFilterField(), criteriaBuilder, root));
						restrictions.add(criteriaBuilder.and(conjunctionRestrictions.toArray(new Predicate[]{})));
					}
				}
				
				currentExpression = currentExpression != null ? currentExpression.getFilterNestedExpression() : currentExpression;
			}
	        
			return restrictions;
		
		} catch (Exception e) {
			throw new BadRequestApiException(String.format(MSGERROR.PARSE_FILTER_FIELDS_ERROR, requestFilter.getFilter()), e);
		}
	}

	/**
	 * Fill the {@link Predicate} with the restrictions and the query related to the 
	 * {@link FilterExpression} of {@link LogicOperator#OR} operator and return the next nested 
	 * expression after the {@link LogicOperator#OR} expression.
	 * 
	 * @param entityClass
	 * @param criteriaBuilder
	 * @param root
	 * @param currentExpression
	 * @param conjunctionRestrictions
	 * @return {@link FilterExpression}
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	private FilterExpression getOrRestrictions(Class<E> entityClass, CriteriaBuilder criteriaBuilder, Root<?> root,
			FilterExpression currentExpression, List<Predicate> conjunctionRestrictions)
			throws NoSuchFieldException, IOException {
		
		do {
			conjunctionRestrictions.add(this.buildPredicate(entityClass, currentExpression.getFilterField(), criteriaBuilder, root));
			currentExpression = currentExpression.getFilterNestedExpression();
		
		} while (currentExpression != null && LogicOperator.OR.equals(currentExpression.getLogicOperator()));
		
		if (currentExpression != null && currentExpression.getFilterField() != null) {
			conjunctionRestrictions.add(this.buildPredicate(entityClass, currentExpression.getFilterField(), criteriaBuilder, root));
		}
		
		return currentExpression;
	}

	/**
	 * Build query {@link Predicate} based on {@link FilterField} field, expected value and a {@link FilterOperator}.
	 * 
	 * @param entityClass
	 * @param filterField
	 * @param criteriaBuilder
	 * @param root
	 * @return Predicate
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	private Predicate buildPredicate(
			Class<E> entityClass,
			FilterField filterField, 
			CriteriaBuilder criteriaBuilder, 
			Root<?> root) throws NoSuchFieldException, IOException {

		List<Field> fields = this.splitFields(entityClass, filterField.getField());
		Field field = null;
		
		switch (filterField.getFilterOperator()) {
			case IN:
				field = this.getSignificantField(fields);
				
				String normalizedValues = StringParserUtils.replace(filterField.getValue(), new String[]{"(", ")"}, "");
				
				return this.getFieldExpressionPath(fields, root)
						.in(ReflectionUtils.getTypifiedValue(StringParserUtils.splitStringList(normalizedValues, ','), field.getType()));
			case OU:
				field = this.getSignificantField(fields);
				
				normalizedValues = StringParserUtils.replace(filterField.getValue(), new String[]{"(", ")"}, "");
				
				return this.getFieldExpressionPath(fields, root)
						.in(ReflectionUtils.getTypifiedValue(StringParserUtils.splitStringList(normalizedValues, ','), field.getType())).not();
			case GE:
				return criteriaBuilder.greaterThanOrEqualTo(this.getFieldExpressionPath(
						fields, root), (Comparable) this.getTypifiedValue(filterField, fields));
			case GT:
				return criteriaBuilder.greaterThan(this.getFieldExpressionPath(
						fields, root), (Comparable) this.getTypifiedValue(filterField, fields));
			case LE:
				return criteriaBuilder.lessThanOrEqualTo(this.getFieldExpressionPath(
						fields, root), (Comparable) this.getTypifiedValue(filterField, fields));
			case LT:
				return criteriaBuilder.lessThan(this.getFieldExpressionPath(
						fields, root), (Comparable) this.getTypifiedValue(filterField, fields));
			case NE:
				if (BaseConstants.NULL_VALUE.equals(filterField.getValue())) {
					return criteriaBuilder.isNotNull(this.getFieldExpressionPath(fields, root));
				}
				return criteriaBuilder.notEqual(this.getFieldExpressionPath(
						fields, root), this.getTypifiedValue(filterField, fields));
			case LK:
				return criteriaBuilder.like(criteriaBuilder.upper(this.getFieldExpressionPath(fields, root)), 
						"%" + ((String) this.getTypifiedValue(filterField, fields)).toUpperCase() + "%");
			case EQ:
			default:
				if (BaseConstants.NULL_VALUE.equals(filterField.getValue())) {
					return criteriaBuilder.isNull(this.getFieldExpressionPath(fields, root));
				}
				return criteriaBuilder.equal(this.getFieldExpressionPath(
						fields, root), this.getTypifiedValue(filterField, fields));
		}
	}

	/**
	 * Split a field into a chained list of its subfields.
	 * 
	 * @param entityClass
	 * @param fieldName
	 * @return List of {@link Field}
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private List<Field> splitFields(Class<E> entityClass, String fieldName) throws NoSuchFieldException, SecurityException {
		List<Field> fields = new ArrayList<>();
		List<String> attributeNames =  StringParserUtils.splitStringList(fieldName, '.');
		Class<?> currentFieldClass = entityClass;
		
		for (String attributeName : attributeNames) {
			Field field = ReflectionUtils.getEntityFieldByName(currentFieldClass, attributeName);
			currentFieldClass = field.getType();
			fields.add(field);
		}
		
		return fields;
	}

	/**
	 * Return the typified value of a leaf last subfield.
	 * 
	 * @param filterField
	 * @param fields
	 * @return The value of the field
	 * @throws IOException
	 * @throws NoSuchFieldException
	 */
	private Object getTypifiedValue(FilterField filterField, List<Field> fields)
			throws IOException, NoSuchFieldException {
		
		Field field = this.getSignificantField(fields);
		return ReflectionUtils.getTypifiedValue(filterField.getValue(), field.getType());
	}

	/**
	 * Return leaf last subfield
	 * 
	 * @param fields
	 * @return {@link Field}
	 * @throws NoSuchFieldException
	 */
	private Field getSignificantField(List<Field> fields) throws NoSuchFieldException {
		if (fields.isEmpty()) {
			throw new NoSuchFieldException();
		}
		
		return fields.get(fields.size() - 1);
	}
	
	/**
	 * Build {@link Expression} of {@link Path} for given fields.
	 * 
	 * @param fields
	 * @param root
	 * @return {@link Expression}
	 * @throws NoSuchFieldException
	 */
	private Expression getFieldExpressionPath(List<Field> fields, Root<?> root) throws NoSuchFieldException {
		Path<E> expressionPath = null;
		
		for (Field field : fields) {
			if (expressionPath == null) {
				expressionPath = root.get(field.getName());
				expressionPath.alias(field.getName());
			} else {
				expressionPath = expressionPath.get(field.getName());
				expressionPath.alias(field.getName());
			}
		}
		
		if (expressionPath == null) {
			throw new NoSuchFieldException();
		}
		
		return expressionPath;
	}
	
	/**
	 * Build a list of {@link Order} from {@link RequestFilter}.
	 * 
	 * @param requestFilter
	 * @param criteriaBuilder
	 * @param root
	 * @param entityClass
	 * @return List of {@link Order}
	 * @throws BadRequestApiException
	 */
	public List<Order> getOrders(
			RequestFilter requestFilter, 
			CriteriaBuilder criteriaBuilder,
			Root<?> root,
			Class<E> entityClass) throws BadRequestApiException {
		
		try {
			List<FilterOrder> filterOrders = FilterOrder.of(requestFilter.getSort());
			List<Order> orders = new ArrayList<>();
	        
			if (filterOrders != null && !filterOrders.isEmpty()) {
	        	
	        	for (FilterOrder filterOrder : filterOrders) {
	        		List<Field> fields =  this.splitFields(entityClass, filterOrder.getField());

	        		switch (filterOrder.getSortOrder()) {
	        			case DESC:
	        				orders.add(criteriaBuilder.desc(this.getFieldExpressionPath(fields, root)));
	        				break;
	        			case ASC:
	        			default:
	        				orders.add(criteriaBuilder.asc(this.getFieldExpressionPath(fields, root)));
	        				break;
	        		}
	        	}
	        }
			
			return orders;
		
		} catch (Exception e) {
			throw new BadRequestApiException(String.format(MSGERROR.PARSE_SORT_ORDER_ERROR, requestFilter.getSort()));
		}
	}
	
}