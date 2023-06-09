package com.generic.rest.core.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.BadRequestApiException;
import com.generic.rest.core.exception.InternalErrorApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.mapper.ApiResultMapper;

@Repository
public class ApiRepository<E extends BaseEntity> {
	
	private EntityManager entityManager;
	private ApiQueryBuilder<E> apiQueryBuilder;
	private ApiResultMapper<E> apiResultMapper;

	@Autowired
	public ApiRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		
		this.apiQueryBuilder = new ApiQueryBuilder<>();
		this.apiResultMapper = new ApiResultMapper<>();
	}
	
	public Long countAll(Class<E> entityClass, RequestFilter requestFilter) 
			throws NotFoundApiException, BadRequestApiException, InternalErrorApiException {
		
		requestFilter.processSymbols();
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<?> root = query.from(entityClass);

		query.select(criteriaBuilder.count(root));
		
		List<Predicate> restrictions = apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		try {
		    return entityManager.createQuery(query).getSingleResult();
		
		} catch (NoResultException e) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITIES_NOT_FOUND_ERROR, requestFilter), e);
		
		} catch (PersistenceException e) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, requestFilter), e);
		
		} catch (Exception e) {
			throw new InternalErrorApiException(String.format(MSGERROR.UNEXPECTED_FETCHING_ERROR, requestFilter), e);
		}
	}
	
	public List<E> findAll(Class<E> entityClass, RequestFilter requestFilter) 
			throws NotFoundApiException, BadRequestApiException, InternalErrorApiException {
		
		requestFilter.processSymbols();

		if (Boolean.TRUE.equals(requestFilter.hasValidAggregateFunction())) {
			return aggregate(entityClass, requestFilter);
		}
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<?> root = query.from(entityClass);

		List<Selection<? extends Object>> projection = apiQueryBuilder.getProjectionFields(requestFilter, root, entityClass);
		if (!projection.isEmpty() && (projection.size() == 1 || !apiQueryBuilder.containsMultiValuedProjection(projection))) {
			query.multiselect(projection.toArray(new Selection[]{}));
		} else {
			query.select(root);
		}
		
		List<Predicate> restrictions = apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		List<Order> orders = apiQueryBuilder.getOrders(requestFilter, criteriaBuilder, root, entityClass);
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}
		
		try {
		    List<Object> result = entityManager.createQuery(query)
		    		.setMaxResults(requestFilter.getFetchLimit())
		    		.setFirstResult(requestFilter.getFetchOffset())
		    		.getResultList();
		    
		    return apiResultMapper.mapResultSet(entityClass, result, projection);
		    
		} catch (NoResultException e) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITIES_NOT_FOUND_ERROR, requestFilter), e);
		
		} catch (PersistenceException | NumberFormatException e) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, requestFilter), e);
			
		} catch (Exception e) {
			throw new InternalErrorApiException(String.format(MSGERROR.UNEXPECTED_FETCHING_ERROR, requestFilter), e);
		}
	}
	
	public List<E> aggregate(Class<E> entityClass, RequestFilter requestFilter) 
			throws NotFoundApiException, BadRequestApiException, InternalErrorApiException {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<?> root = query.from(entityClass);

		List<Selection<? extends Object>> aggregationFields = apiQueryBuilder.buildAggregateSelection(root, criteriaBuilder, entityClass, requestFilter);
		
		if (aggregationFields.isEmpty()) {
			throw new BadRequestApiException(String.format(MSGERROR.INVALID_AGGREGATION_ERROR, requestFilter));
		}
		
		query.multiselect(aggregationFields.toArray(new Selection[]{}));
		
		List<Predicate> restrictions = apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		List<Order> orders = apiQueryBuilder.getOrders(requestFilter, criteriaBuilder, root, entityClass);
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}
		
		List<Selection<? extends Object>> groupBy = apiQueryBuilder.getGroupByFields(requestFilter, root, entityClass);
		query.groupBy(groupBy.toArray(new Expression[]{}));
		
		try {
		    List<Object> result = entityManager.createQuery(query)
		    		.getResultList();
		    
		    return apiResultMapper.mapResultSet(entityClass, result, aggregationFields);
		    
		} catch (NoResultException e) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITIES_NOT_FOUND_ERROR, requestFilter), e);
		
		} catch (PersistenceException | ReflectiveOperationException e) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, requestFilter), e);
			
		} catch (Exception e) {
			throw new InternalErrorApiException(String.format(MSGERROR.UNEXPECTED_FETCHING_ERROR, requestFilter), e);
		}
	}

}