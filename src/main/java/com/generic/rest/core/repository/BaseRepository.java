package com.generic.rest.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.generic.rest.core.domain.BaseEntity;

/**
 * Base Repository that extends {@link JpaRepository} basic functions.
 * 
 * @author leonardo.ramos
 *
 * @param <E>
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {
	
}