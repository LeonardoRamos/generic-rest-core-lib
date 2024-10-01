package com.generic.rest.core.repository.query.builder;

import java.util.List;

import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.BadRequestApiException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

/**
 * Interface responsible for exposing methods for building JPA query related filters.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
public interface QueryBuilder<E> {

	/**
	 * Verify if given projection has any collection / multi valued field.
	 * 
	 * @param projection
	 * @return true if selection has multi valued / collection projection, false otherwise.
	 */
	boolean containsMultiValuedProjection(List<Selection<? extends Object>> projection);

	/**
	 * Build group by fields selection from {@link RequestFilter}.
	 * 
	 * @param requestFilter
	 * @param root
	 * @param entityClass
	 * @return Selection of group by fields
	 * @throws BadRequestApiException
	 */
	List<Selection<? extends Object>> getGroupByFields(RequestFilter requestFilter, Root<?> root, Class<E> entityClass)
			throws BadRequestApiException;

	/**
	 * Build projection fields selection from {@link RequestFilter}.
	 * 
	 * @param requestFilter
	 * @param root
	 * @param entityClass
	 * @return Selection of projection fields
	 * @throws BadRequestApiException
	 */
	List<Selection<? extends Object>> getProjectionFields(RequestFilter requestFilter, Root<?> root,
			Class<E> entityClass) throws BadRequestApiException;

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
	List<Selection<? extends Object>> getAggregateSelection(Root<?> root, CriteriaBuilder criteriaBuilder,
			Class<E> entityClass, RequestFilter requestFilter) throws BadRequestApiException;

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
	List<Predicate> getRestrictions(Class<E> entityClass, RequestFilter requestFilter, CriteriaBuilder criteriaBuilder,
			Root<?> root) throws BadRequestApiException;

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
	List<Order> getOrders(RequestFilter requestFilter, CriteriaBuilder criteriaBuilder, Root<?> root,
			Class<E> entityClass) throws BadRequestApiException;

}