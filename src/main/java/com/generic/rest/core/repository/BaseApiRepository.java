package com.generic.rest.core.repository;

import java.util.Calendar;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.generic.rest.core.domain.BaseApiEntity;

@NoRepositoryBean
public interface BaseApiRepository<E extends BaseApiEntity> extends BaseRepository<E> {
	
	@Transactional
	@Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} as E SET E.active = false, E.deleteDate = :deleteDate WHERE E.externalId = :externalId")
    Long logicDelete(@Param("externalId") String externalId, @Param("deleteDate") Calendar deleteDate);

	@Transactional
	Integer deleteByExternalId(String externalId);

	E findOneByExternalId(String externalId);
	
}