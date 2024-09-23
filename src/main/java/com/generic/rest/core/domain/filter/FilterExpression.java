package com.generic.rest.core.domain.filter;

public class FilterExpression {
    
    private FilterField filterField;
    private FilterExpression nestedFilterExpression;
    private LogicOperator logicOperator;
    
    public FilterField getFilterField() {
        return filterField;
    }
    
    public void setFilterField(FilterField filterField) {
        this.filterField = filterField;
    }
    
    public FilterExpression getFilterNestedExpression() {
        return nestedFilterExpression;
    }
    
    public void setNestedFilterExpression(FilterExpression nestedFilterExpression) {
        this.nestedFilterExpression = nestedFilterExpression;
    }

	public LogicOperator getLogicOperator() {
		return logicOperator;
	}

	public void setLogicOperator(LogicOperator logicOperator) {
		this.logicOperator = logicOperator;
	}
	
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
	
	public FilterExpression appendNewNestedExpression() {
		if (this.getLogicOperator() != null) {
    		
			FilterExpression newExpression = new FilterExpression();
    		
			this.setNestedFilterExpression(newExpression);
    	}
		
		return this.getFilterNestedExpression();
	}
	
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
	
	@Override
	public String toString() {
		return "Expression [filterField=" + filterField + ", logicOperator=" + logicOperator + 
				", expression=" + nestedFilterExpression +  "]";
	}
	
}
