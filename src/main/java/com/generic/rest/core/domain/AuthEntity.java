package com.generic.rest.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;

public interface AuthEntity {

	@Transient
	String getExternalId();
	
	@Transient
	@JsonIgnore
	String getPrincipalCredential();
	
	@Transient
	@JsonIgnore
	String getCredentialRole();
	
	@Transient
	@JsonIgnore
	String getAdditionalInfo();
	
}