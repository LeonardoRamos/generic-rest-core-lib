package com.generic.rest.core.domain.filter;

import java.util.Arrays;

/**
 * Enumeration that maps query filter operations.
 * 
 * @author leonardo.ramos
 *
 */
public enum FilterOperator {
	
	EQ("=eq=",  "=", "|eq|"), 
	LE("=le=", "<=", "|le|"), 
	GE("=ge=", ">=", "|ge|"), 
	GT("=gt=", ">", "|gt|"), 
	LT("=lt=", "<", "|lt|"), 
	NE("=ne=", "!=", "|ne|"),
	IN("=in=", "=in=", "|in|"),
	OU("=out=", "=out=", "|ou|"),
	LK("=like=", "=like=", "|lk|");
	
	private final String operatorAlias;
	private final String operatorCommonAlias;
	private final String parseableOperator;

	/**
	 * Constructor.
	 * 
	 * @param operatorAlias
	 * @param operatorCommonAlias
	 * @param parseableOperator
	 */
	FilterOperator(String operatorAlias, String operatorCommonAlias, String parseableOperator) {
		this.operatorAlias = operatorAlias;
		this.operatorCommonAlias = operatorCommonAlias;
		this.parseableOperator = parseableOperator;
	}
	
	/**
	 * Return {@link FilterOperator} for given String representation.
	 * 
	 * @param operator
	 * @return {@link FilterOperator}
	 */
	public static FilterOperator of(String operator) {
		return Arrays.stream(values())
					.filter(fo -> fo.name().equalsIgnoreCase(operator) || fo.getOperatorAlias().equalsIgnoreCase(operator)
							|| fo.getOperatorCommonAlias().equalsIgnoreCase(operator)
							|| fo.getParseableOperator().equalsIgnoreCase(operator))
					.findFirst()
					.orElse(null);
	}
	
	/**
	 * Return the operator alias.
	 * 
	 * @return the operator alias
	 */
	public String getOperatorAlias() {
		return operatorAlias;
	}
	
	/**
	 * Return the operator commom alias.
	 * 
	 * @return the operator commom alias
	 */
	public String getOperatorCommonAlias() {
		return operatorCommonAlias;
	}

	/**
	 * Return the parseable operator.
	 * 
	 * @return the parseable operator
	 */
	public String getParseableOperator() {
		return parseableOperator;
	}
	
}