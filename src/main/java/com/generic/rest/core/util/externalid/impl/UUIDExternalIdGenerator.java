package com.generic.rest.core.util.externalid.impl;

import java.util.UUID;

import com.generic.rest.core.util.StringParserUtils;
import com.generic.rest.core.util.externalid.ExternalIdGenerator;

/**
 * Implementation of {@link ExternalIdGenerator} for {@link UUID} based ExternalId.
 * 
 * @author leonardo.ramos
 *
 */
public class UUIDExternalIdGenerator implements ExternalIdGenerator {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate() {
		return StringParserUtils.replace(UUID.randomUUID().toString(), "-", "");
	}

}