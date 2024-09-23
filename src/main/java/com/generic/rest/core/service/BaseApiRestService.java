package com.generic.rest.core.service;

import java.util.Calendar;

import com.generic.rest.core.BaseConstants.MSGERROR;
import com.generic.rest.core.domain.BaseApiEntity;
import com.generic.rest.core.exception.ApiException;
import com.generic.rest.core.exception.NotFoundApiException;
import com.generic.rest.core.repository.BaseApiRepository;

public abstract class BaseApiRestService<E extends BaseApiEntity, R extends BaseApiRepository<E>> 
	extends ApiRestService<E, R>{
	
	public E getByExternalId(String externalId) throws NotFoundApiException {
		E entity = this.getRepository().findOneByExternalId(externalId);
		
		if (entity == null) {
			throw new NotFoundApiException(String.format(MSGERROR.ENTITY_NOT_FOUND_ERROR, externalId));
		}
		
		return entity;
	}
	
	@Override
	public E update(E entity) throws ApiException {
		if (entity.getId() == null) {
			E entityDatabase = getByExternalId(entity.getExternalId());
			entity.setId(entityDatabase.getId());
		}

		entity.setUpdateDate(Calendar.getInstance());
      
		if (entity.isActive()) {
			entity.setDeleteDate(null);
		}
      
		return this.getRepository().saveAndFlush(entity);
	}

	public Integer delete(String externalId) throws ApiException {
	   	Integer deletedCount = this.getRepository().deleteByExternalId(externalId);
	   
	   	if (deletedCount == 0) {
		   	throw new NotFoundApiException(String.format(MSGERROR.ENTITY_NOT_FOUND_ERROR, externalId));
	   	}
	   
	   	return deletedCount;
   	}
   
	@Override
   	public E save(E entity) throws ApiException {
   		if (entity.getExternalId() == null || "".equals(entity.getExternalId())) {
   			entity.setExternalId(getExternalIdGenerator().generate());
   		}
   		
   		entity.setInsertDate(Calendar.getInstance());
   		entity.setUpdateDate(entity.getInsertDate());
   		entity.setDeleteDate(null);

	   	return this.getRepository().saveAndFlush(entity);
   	}
	
}