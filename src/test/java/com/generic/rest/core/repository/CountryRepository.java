package com.generic.rest.core.repository;

import org.springframework.stereotype.Repository;

import com.generic.rest.core.domain.Country;

@Repository
public interface CountryRepository extends BaseApiRepository<Country> {
	
	Country findByName(String name);

}