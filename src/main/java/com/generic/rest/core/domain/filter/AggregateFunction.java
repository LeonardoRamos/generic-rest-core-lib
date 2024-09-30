package com.generic.rest.core.domain.filter;

import java.util.Arrays;

/**
 * Enumeration that maps aggregation functions operations.
 * 
 * @author leonardo.ramos
 *
 */
public enum AggregateFunction {
	
	SUM("sum"), 
	AVG("avg"), 
	COUNT("count"), 
	COUNT_DISTINCT("count_distinct");
	
	private final String function;

	/**
	 * Constructor
	 * 
	 * @param function
	 */
	AggregateFunction(String function) {
		this.function = function;
	}
	
	/**
	 * Return the function.
	 * 
	 * @return function
	 */
	public String getFunction() {
		return function;
	}
	
	/**
	 * Return {@link AggregateFunction} for given String representation.
	 * 
	 * @param function
	 * @return {@link AggregateFunction}
	 */
	public static AggregateFunction of(String function) {
		return Arrays.stream(values())
				.filter(af -> af.name().equalsIgnoreCase(function) || 
						af.getFunction().equalsIgnoreCase(function))
				.findFirst()
				.orElse(null); 
	}
	
	/**
	 * Verify if String matches {@link AggregateFunction#SUM} function.
	 * 
	 * @param aggregateFunction
	 * @return true if string matches {@link AggregateFunction#SUM}, false otherwise
	 */
	public static boolean isSumFunction(String aggregateFunction) {
		return aggregateFunction != null && (SUM.name().equals(aggregateFunction) || SUM.getFunction().equals(aggregateFunction) ||
				SUM.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	/**
	 * Verify if String matches {@link AggregateFunction#AVG} function.
	 * 
	 * @param aggregateFunction
	 * @return true if string matches {@link AggregateFunction#AVG}, false otherwise
	 */
	public static boolean isAvgFunction(String aggregateFunction) {
		return aggregateFunction != null && (AVG.name().equals(aggregateFunction) || AVG.getFunction().equals(aggregateFunction) ||
				AVG.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	/**
	 * Verify if String matches {@link AggregateFunction#COUNT} function.
	 * 
	 * @param aggregateFunction
	 * @return true if string matches {@link AggregateFunction#COUNT}, false otherwise
	 */
	public static boolean isCountFunction(String aggregateFunction) {
		return aggregateFunction != null && (COUNT.name().equals(aggregateFunction) || COUNT.getFunction().equals(aggregateFunction) ||
				COUNT.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	/**
	 * Verify if String matches {@link AggregateFunction#COUNT_DISTINCT} function.
	 * 
	 * @param aggregateFunction
	 * @return true if string matches {@link AggregateFunction#COUNT_DISTINCT}, false otherwise
	 */
	public static boolean isCountDistinctFunction(String aggregateFunction) {
		return aggregateFunction != null && (COUNT_DISTINCT.name().equals(aggregateFunction) || COUNT_DISTINCT.getFunction().equals(aggregateFunction) ||
				COUNT_DISTINCT.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
}