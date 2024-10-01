package com.generic.rest.core.domain.filter;

import java.util.Arrays;

/**
 * Enumeration that maps filter logic operations.
 * 
 * @author leonardo.ramos
 *
 */
public enum LogicOperator {
	
	OR("_or_", ","), 
	AND("_and_", ";");
	
	private final String operator;
	private final String operatorAlias;

	/**
	 * Constructor.
	 * 
	 * @param operator
	 * @param operatorAlias
	 */
	LogicOperator(String operator, String operatorAlias) {
		this.operator = operator;
		this.operatorAlias = operatorAlias;
	}
	
	public String getOperator() {
		return operator;
	}

	/**
	 * Return the operator alias.
	 * 
	 * @return operatorAlias
	 */
	public String getOperatorAlias() {
		return operatorAlias;
	}
	
	/**
	 * Return {@link LogicOperator} for given String representation.
	 * 
	 * @param logicalOperator
	 * @return {@link LogicOperator}
	 */
	public static LogicOperator of(String logicalOperator) {
		return Arrays.stream(values())
					.filter(lo -> lo.operator.equalsIgnoreCase(logicalOperator) || lo.operatorAlias.equalsIgnoreCase(logicalOperator) || 
							lo.name().equalsIgnoreCase(logicalOperator))
					.findFirst()
					.orElse(null);
	}
	
	/**
	 * Verify if String matches {@link LogicOperator#OR} operator.
	 * 
	 * @param logicalOperator
	 * @return true if string matches {@link LogicOperator#OR}, false otherwise
	 */
	public static boolean isOrOperator(String logicalOperator) {
		return OR.operator.equalsIgnoreCase(logicalOperator) || OR.operatorAlias.equalsIgnoreCase(logicalOperator) || 
				OR.name().equalsIgnoreCase(logicalOperator);
	}
	
	/**
	 * Verify if String matches {@link LogicOperator#AND} operator.
	 * 
	 * @param logicalOperator
	 * @return true if string matches {@link LogicOperator#AND}, false otherwise
	 */
	public static boolean isAndOperator(String logicalOperator) {
		return AND.operator.equalsIgnoreCase(logicalOperator) || AND.operatorAlias.equalsIgnoreCase(logicalOperator) ||
				AND.name().equalsIgnoreCase(logicalOperator);
	}
	
}