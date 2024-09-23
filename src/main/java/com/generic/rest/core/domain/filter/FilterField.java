package com.generic.rest.core.domain.filter;

public class FilterField {
	
	private String field;
	private String value;
	private FilterOperator filterOperator;
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}
	
	public static FilterField of(String logicExpression) {
		FilterField filterField = new FilterField();
		
		if (logicExpression == null || "".equals(logicExpression)) {
			return null;
		}
		
        StringBuilder word = new StringBuilder();
        Integer i = 0;
        
        while (i < logicExpression.length()) {
          
    		if (logicExpression.charAt(i) != '|') {
                word.append(logicExpression.charAt(i));
            
    		} else {
                FilterOperator filterOperator = getComparisonOperator(logicExpression, i);
                
                filterField.setField(word.toString().trim());
                filterField.setFilterOperator(filterOperator);
                
                i += filterOperator.getParseableOperator().length() - 1;
                word = new StringBuilder();
    		}
    		
    		i++;
        }
        
        filterField.setValue(word.toString().trim());
        
        return filterField;
	}
	
	private static FilterOperator getComparisonOperator(String logicExpression, int i) {
		StringBuilder operation = new StringBuilder();
		boolean appendOperation = true; 
		
		do {
			operation.append(logicExpression.charAt(i));
		    i++;
			
		    if (i >= logicExpression.length() || logicExpression.charAt(i) == '|') {
		    	
		    	appendOperation = false;
		    	
		    	if (i < logicExpression.length() && logicExpression.charAt(i) == '|') {
		    		operation.append(logicExpression.charAt(i));
		    	}
		    }
			
		} while (appendOperation);
		
		return FilterOperator.of(operation.toString().trim());
	}

	@Override
	public String toString() {
		return "FilterField [field=" + field + ", value=" + value + ", filterOperator=" + filterOperator + "]";
	}
	
}