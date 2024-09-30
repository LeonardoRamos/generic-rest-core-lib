package com.generic.rest.core.util.externalid;

/**
 * Interface for generating custom ExternalId.
 * 
 * @author leonardo.ramos
 *
 */
public interface ExternalIdGenerator {
	
	/**
	 * Generate ExternalId.
	 * 
	 * @return externalId
	 */
	public String generate();

}