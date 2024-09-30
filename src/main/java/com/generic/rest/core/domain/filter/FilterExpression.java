package com.generic.rest.core.domain.filter;

/**
 * Class responsible for representing a filter query expression.
 * 
 * @author leonardo.ramos
 *
 */
public class FilterExpression {
    
    private FilterField filterField;
    private FilterExpression nestedFilterExpression;
    private LogicOperator logicOperator;
    
    /**
     * Return the filter field.
     * 
     * @return filterField
     */
    public FilterField getFilterField() {
        return filterField;
    }
    
    /**
     * Set filter field.
     * 
     * @param filterField
     */
    public void setFilterField(FilterField filterField) {
        this.filterField = filterField;
    }
    
    /**
     * Return the nested filter expression.
     * 
     * @return nestedFilterExpression
     */
    public FilterExpression getFilterNestedExpression() {
        return nestedFilterExpression;
    }
    
    /**
     * Set the nested filter expression.
     * 
     * @param nestedFilterExpression
     */
    public void setNestedFilterExpression(FilterExpression nestedFilterExpression) {
        this.nestedFilterExpression = nestedFilterExpression;
    }

	/**
	 * Return the {@link LogicOperator} of the expression.
	 * 
	 * @return {@link LogicOperator}
	 */
	public LogicOperator getLogicOperator() {
		return logicOperator;
	}

	/**
	 * Set the {@link LogicOperator} of the expression.
	 * 
	 * @param logicOperator
	 */
	public void setLogicOperator(LogicOperator logicOperator) {
		this.logicOperator = logicOperator;
	}
	
	/**
	 * Parse an expression String into a valid chain of {@link FilterExpression}. 
	 * 
	 * @param expression
	 * @return {@link FilterExpression}
	 */
	public static FilterExpression of(String expression) {
		FilterExpression currentExpression = new FilterExpression();
        
		if (expression == null) {
			return currentExpression;
		}
		
		FilterExpression initialExpression = currentExpression;
        StringBuilder word = new StringBuilder();
        
        Integer i = 0;
        
        while (i < expression.length()) {
            
        	if (expression.charAt(i) != '_') {
                word.append(expression.charAt(i));
                
            } else {
            	LogicOperator logicOperator = parseNextOperator(expression, i);

            	if (logicOperator != null) {
            		currentExpression.setLogicOperator(logicOperator);
            		currentExpression.setFilterField(FilterField.of(word.toString().trim()));
            		currentExpression = currentExpression.appendNewNestedExpression();
            		
            		i += logicOperator.getOperator().length() - 1;
            		word = new StringBuilder();
            	
            	} else {
            		word.append(expression.charAt(i));
            	}
            }
        	
        	i++;
        }
        
        currentExpression.setFilterField(FilterField.of(word.toString().trim()));
        currentExpression.appendNewNestedExpression();
        
        return initialExpression;
	}
	
	/**
	 * Append a new empty nested filter expression to an existing chain of expressions. </p>
	 * Returns the new empty nested expression.
	 * 
	 * @return {@link FilterExpression}
	 */
	public FilterExpression appendNewNestedExpression() {
		if (this.getLogicOperator() != null) {
    		
			FilterExpression newExpression = new FilterExpression();
    		
			this.setNestedFilterExpression(newExpression);
    	}
		
		return this.getFilterNestedExpression();
	}
	
	/**
	 * Parse the next valid {@link LogicOperator} from an ongoing parsing of expression at a given expression String index.
	 * 
	 * @param expression
	 * @param index
	 * @return {@link LogicOperator}
	 */
	private static LogicOperator parseNextOperator(String expression, int index) {
		StringBuilder logicOperator = new StringBuilder();
		boolean appendOperation = true;
		
		do {
			logicOperator.append(expression.charAt(index));
		    index++;
		    
		    if (index >= expression.length() || expression.charAt(index) == '_') {
		    	
		    	appendOperation = false;
		    	
		    	if (index < expression.length() && expression.charAt(index) == '_') {
		    		logicOperator.append(expression.charAt(index));
		    	}
		    }
		    
		} while (appendOperation);
		
		return LogicOperator.of(logicOperator.toString().trim());
	}
	
	/**
	 * Filter expression toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "Expression [filterField=" + filterField + ", logicOperator=" + logicOperator + 
				", expression=" + nestedFilterExpression +  "]";
	}
	
}
