package com.generic.rest.core.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseEntity;
import com.generic.rest.core.domain.filter.RequestFilter;
import com.generic.rest.core.exception.BadRequestApiException;
import com.generic.rest.core.exception.InternalErrorApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.mapper.ApiResultMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

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
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<?> root = query.from(entityClass);

		query.select(criteriaBuilder.count(root));
		
		List<Predicate> restrictions = this.apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		try {
		    return this.entityManager.createQuery(query).getSingleResult();
		
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
		
		if (requestFilter.hasValidAggregateFunction()) {
			return aggregate(entityClass, requestFilter);
		}
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<?> root = query.from(entityClass);

		List<Selection<? extends Object>> projection = this.apiQueryBuilder.getProjectionFields(requestFilter, root, entityClass);
		
		if (!projection.isEmpty() && (projection.size() == 1 || !this.apiQueryBuilder.containsMultiValuedProjection(projection))) {
			query.multiselect(projection.toArray(new Selection[]{}));
		} else {
			query.select(root);
		}
		
		List<Predicate> restrictions = this.apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		List<Order> orders = this.apiQueryBuilder.getOrders(requestFilter, criteriaBuilder, root, entityClass);
		
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}
		
		try {
		    List<Object> result = this.entityManager.createQuery(query)
		    		.setMaxResults(requestFilter.getFetchLimit())
		    		.setFirstResult(requestFilter.getFetchOffset())
		    		.getResultList();
		    
		    return this.apiResultMapper.mapResultSet(entityClass, result, projection);
		    
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
		
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
		Root<?> root = query.from(entityClass);

		List<Selection<? extends Object>> aggregationFields = this.apiQueryBuilder.getAggregateSelection(root, criteriaBuilder, entityClass, requestFilter);
		
		if (aggregationFields.isEmpty()) {
			throw new BadRequestApiException(String.format(MSGERROR.INVALID_AGGREGATION_ERROR, requestFilter));
		}
		
		query.multiselect(aggregationFields.toArray(new Selection[]{}));
		
		List<Predicate> restrictions = this.apiQueryBuilder.getRestrictions(entityClass, requestFilter, criteriaBuilder, root); 
		
		if (!restrictions.isEmpty()) {
			query.where(restrictions.toArray(new Predicate[]{}));
		}
		
		List<Order> orders = this.apiQueryBuilder.getOrders(requestFilter, criteriaBuilder, root, entityClass);
		
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}
		
		List<Selection<? extends Object>> groupBy = this.apiQueryBuilder.getGroupByFields(requestFilter, root, entityClass);
		query.groupBy(groupBy.toArray(new Expression[]{}));
		
		try {
		    List<Object> result = this.entityManager.createQuery(query)
		    		.getResultList();
		    
		    return this.apiResultMapper.mapResultSet(entityClass, result, aggregationFields);
		    
		} catch (NoResultException e) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITIES_NOT_FOUND_ERROR, requestFilter), e);
		
		} catch (PersistenceException | ReflectiveOperationException e) {
			throw new BadRequestApiException(String.format(MSGERROR.BAD_REQUEST_ERROR, requestFilter), e);
			
		} catch (Exception e) {
			throw new InternalErrorApiException(String.format(MSGERROR.UNEXPECTED_FETCHING_ERROR, requestFilter), e);
		}
	}

}