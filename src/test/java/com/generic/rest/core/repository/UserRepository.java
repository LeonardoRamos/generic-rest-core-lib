package com.generic.rest.core.repository;

import org.springframework.stereotype.Repository;

import com.generic.rest.core.domain.User;

@Repository
public interface UserRepository extends BaseApiRepository<User> {
	
	User findByEmailAndActive(String email, boolean active);

}