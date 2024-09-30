package com.generic.rest.core.repository;

import java.util.Calendar;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.generic.rest.core.domain.BaseApiEntity;

/**
 * Base Api Repository that extends {@link BaseRepository} basic functions and add other operations based on ExternalId.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@NoRepositoryBean
public interface BaseApiRepository<E extends BaseApiEntity> extends BaseRepository<E> {
	
	/**
	 * Soft delete entity from database.
	 * 
	 * @param externalId
	 * @param deleteDate
	 * @return Number of soft deleted entities.
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} as E SET E.active = false, E.deleteDate = :deleteDate WHERE E.externalId = :externalId")
    Long logicDelete(@Param("externalId") String externalId, @Param("deleteDate") Calendar deleteDate);

	/**
	 * Delete entity with given ExternalId.
	 * 
	 * @param externalId
	 * @return Number of deleted entities.
	 */
	@Transactional
	Integer deleteByExternalId(String externalId);

	/**
	 * Find entity with given ExternalId.
	 * 
	 * @param externalId
	 * @return E
	 */
	E findOneByExternalId(String externalId);
	
}