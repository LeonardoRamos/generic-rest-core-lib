package com.generic.rest.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;

/**
 * Basic entity for authentication token claim fields
 * 
 * @author leonardo.ramos
 *
 */
public interface AuthEntity {

	/**
	 * Return the ExternalId for token claim.
	 * 
	 * @return the externalId
	 */
	@Transient	
	String getExternalId();
	
	/**
	 * Return field value for principal credential for token claim.
	 * 
	 * @return the principal credential
	 */
	@Transient
	@JsonIgnore
	String getPrincipalCredential();
	
	/**
	 * Return the credential role value for token claim.
	 * 
	 * @return credential role
	 */
	@Transient
	@JsonIgnore
	String getCredentialRole();
	
	/**
	 * Return any additional info for token claim
	 * 
	 * @return additional info
	 */
	@Transient
	@JsonIgnore
	String getAdditionalInfo();
	
}