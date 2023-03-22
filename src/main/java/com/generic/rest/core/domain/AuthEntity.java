package com.generic.rest.core.domain;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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