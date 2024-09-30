package com.generic.rest.core.domain.filter;

import java.util.Arrays;

/**
 * Enumeration that maps sorting data operations.
 * 
 * @author leonardo.ramos
 *
 */
public enum SortOrder {
	
	ASC("asc"), 
	DESC("desc");
	
	private final String order;

	/**
	 * Constructor.
	 * 
	 * @param order
	 */
	SortOrder(String order) {
		this.order = order;
	}
	
	/**
	 * Return the sorting order String value.
	 * 
	 * @return the sorting order
	 */
	public String getOrder() {
		return order;
	}
	
	/**
	 * Return {@link SortOrder} for given String representation.
	 * 
	 * @param order
	 * @return {@link SortOrder}
	 */
	public static SortOrder of(String order) {
		return Arrays.stream(values())
				.filter(so -> so.name().equalsIgnoreCase(order) || so.getOrder().equalsIgnoreCase(order))
				.findFirst()
				.orElse(null);
	}
	
}