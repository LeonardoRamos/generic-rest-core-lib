package com.generic.rest.core.util.external.id.impl;

import java.util.UUID;

import com.generic.rest.core.util.StringParserUtils;
import com.generic.rest.core.util.external.id.ExternalIdGenerator;

/**
 * Implementation of {@link ExternalIdGenerator} for {@link UUID} based ExternalId.
 * 
 * @author leonardo.ramos
 *
 */
public class UUIDExternalIdGenerator implements ExternalIdGenerator {
	
	/**
	 * Generate ExternalId.
	 * 
	 * @return externalId
	 */
	@Override
	public String get() {
		return StringParserUtils.replace(UUID.randomUUID().toString(), "-", "");
	}

}