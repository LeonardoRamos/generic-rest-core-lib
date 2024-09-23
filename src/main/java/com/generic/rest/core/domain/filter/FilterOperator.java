package com.generic.rest.core.domain.filter;

import java.util.Arrays;

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

	FilterOperator(String operatorAlias, String operatorCommonAlias, String parseableOperator) {
		this.operatorAlias = operatorAlias;
		this.operatorCommonAlias = operatorCommonAlias;
		this.parseableOperator = parseableOperator;
	}
	
	public static FilterOperator of(String operator) {
		return Arrays.stream(values())
					.filter(fo -> fo.name().equalsIgnoreCase(operator) || fo.getOperatorAlias().equalsIgnoreCase(operator)
							|| fo.getOperatorCommonAlias().equalsIgnoreCase(operator)
							|| fo.getParseableOperator().equalsIgnoreCase(operator))
					.findFirst()
					.orElse(null);
	}
	
	public String getOperatorAlias() {
		return operatorAlias;
	}
	
	public String getOperatorCommonAlias() {
		return operatorCommonAlias;
	}

	public String getParseableOperator() {
		return parseableOperator;
	}
	
}