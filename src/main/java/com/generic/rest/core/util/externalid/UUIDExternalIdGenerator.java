package com.generic.rest.core.util.externalid;

import java.util.UUID;

import com.generic.rest.core.util.StringParserUtils;

public class UUIDExternalIdGenerator implements ExternalIdGenerator {
	
	@Override
	public String generate() {
		return StringParserUtils.replace(UUID.randomUUID().toString(), "-", "");
	}

}