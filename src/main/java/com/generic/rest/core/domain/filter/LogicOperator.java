package com.generic.rest.core.domain.filter;

import java.util.Arrays;

public enum LogicOperator {
	
	OR("_or_", ","), 
	AND("_and_", ";");
	
	private final String operator;
	private final String operatorAlias;

	LogicOperator(String operator, String operatorAlias) {
		this.operator = operator;
		this.operatorAlias = operatorAlias;
	}
	
	public String getOperator() {
		return operator;
	}

	public String getOperatorAlias() {
		return operatorAlias;
	}
	
	public static LogicOperator of(String logicalOperator) {
		return Arrays.stream(values())
					.filter(lo -> lo.operator.equalsIgnoreCase(logicalOperator) || lo.operatorAlias.equalsIgnoreCase(logicalOperator) || 
							lo.name().equalsIgnoreCase(logicalOperator))
					.findFirst()
					.orElse(null);
	}
	
	public static boolean isOrOperator(String logicalOperator) {
		return OR.operator.equalsIgnoreCase(logicalOperator) || OR.operatorAlias.equalsIgnoreCase(logicalOperator) || 
				OR.name().equalsIgnoreCase(logicalOperator);
	}
	
	public static boolean isAndOperator(String logicalOperator) {
		return AND.operator.equalsIgnoreCase(logicalOperator) || AND.operatorAlias.equalsIgnoreCase(logicalOperator) ||
				AND.name().equalsIgnoreCase(logicalOperator);
	}
	
}