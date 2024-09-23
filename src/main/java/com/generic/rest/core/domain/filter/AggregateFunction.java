package com.generic.rest.core.domain.filter;

import java.util.Arrays;

public enum AggregateFunction {
	
	SUM("sum"), 
	AVG("avg"), 
	COUNT("count"), 
	COUNT_DISTINCT("count_distinct");
	
	private final String function;

	AggregateFunction(String function) {
		this.function = function;
	}
	
	public String getFunction() {
		return function;
	}
	
	public static AggregateFunction of(String function) {
		return Arrays.stream(values())
				.filter(af -> af.name().equalsIgnoreCase(function) || 
						af.getFunction().equalsIgnoreCase(function))
				.findFirst()
				.orElse(null); 
	}
	
	public static boolean isSumFunction(String aggregateFunction) {
		return aggregateFunction != null && (SUM.name().equals(aggregateFunction) || SUM.getFunction().equals(aggregateFunction) ||
				SUM.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	public static boolean isAvgFunction(String aggregateFunction) {
		return aggregateFunction != null && (AVG.name().equals(aggregateFunction) || AVG.getFunction().equals(aggregateFunction) ||
				AVG.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	public static boolean isCountFunction(String aggregateFunction) {
		return aggregateFunction != null && (COUNT.name().equals(aggregateFunction) || COUNT.getFunction().equals(aggregateFunction) ||
				COUNT.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
	public static boolean isCountDistinctFunction(String aggregateFunction) {
		return aggregateFunction != null && (COUNT_DISTINCT.name().equals(aggregateFunction) || COUNT_DISTINCT.getFunction().equals(aggregateFunction) ||
				COUNT_DISTINCT.equals(AggregateFunction.valueOf(aggregateFunction.toUpperCase())));
	}
	
}