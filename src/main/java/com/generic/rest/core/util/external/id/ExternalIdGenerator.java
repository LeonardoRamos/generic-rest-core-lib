package com.generic.rest.core.util.external.id;

import java.util.function.Supplier;

/**
 * Interface for generating custom ExternalId.
 * 
 * @author leonardo.ramos
 *
 */
public interface ExternalIdGenerator extends Supplier<String> {
	
}