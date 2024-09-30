package com.generic.rest.core.domain.filter;

/**
 * Class responsible for representing a filter field of an expression {@link FilterExpression}.
 * 
 * @author leonardo.ramos
 *
 */
public class FilterField {
	
	private String field;
	private String value;
	private FilterOperator filterOperator;
	
	/**
	 * Return the field name.
	 * 
	 * @return field
	 */
	public String getField() {
		return field;
	}

	/**
	 * Set the field name.
	 * 
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Return the value of field from the expression {@link FilterExpression}.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value of the feld from the expression {@link FilterExpression}.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Return the {@link FilterOperator} of the expression {@link FilterExpression}.
	 * 
	 * @return {@link FilterOperator}
	 */
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	/**
	 * Set the {@link FilterOperator} of the expression {@link FilterExpression}.
	 * 
	 * @param filterOperator
	 */
	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}
	
	/**
	 * Parse an expression String into a valid {@link FilterField}. 
	 * 
	 * @param logicExpression
	 * @return {@link FilterField}
	 */
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
	
	/**
	 * Parse the next valid {@link FilterOperator} from an ongoing parsing of filter field String index.
	 * 
	 * @param logicExpression
	 * @param i
	 * @return {@link FilterOperator}
	 */
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

	/**
	 * Filter field toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "FilterField [field=" + field + ", value=" + value + ", filterOperator=" + filterOperator + "]";
	}
	
}