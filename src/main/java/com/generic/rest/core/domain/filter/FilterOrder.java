package com.generic.rest.core.domain.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for having sorting data.
 * 
 * @author leonardo.ramos
 *
 */
public class FilterOrder {
    
    private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.ASC;
    
	private String field;
    private SortOrder sortOrder;
	
    /**
     * Return field.
     * 
     * @return field
     */
    public String getField() {
		return field;
	}
	
	/**
	 * Set field.
	 * 
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	/**
	 * Return sort order.
	 * 
	 * @return {@link SortOrder}
	 */
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	/**
	 * Set sort order.
	 * 
	 * @param sortOrder
	 */
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	/**
	 * Parse parameter sort of class {@link RequestFilter} and return a list of parsed {@link FilterOrder}. </p>
	 * Default sorting order is {@link SortOrder#ASC}.
	 * 
	 * @param sort
	 * @return List of {@link FilterOrder}
	 */
	public static List<FilterOrder> of(String sort) {
		List<FilterOrder> filterOrders = new ArrayList<>();
		
		if (sort == null) {
			return filterOrders;
		}
		
		FilterOrder filterOrder = new FilterOrder();
		StringBuilder sortField = new StringBuilder();
		
		for (int i = 0; i < sort.length(); i++) {
			
			if (sort.charAt(i) != ',') {
				sortField.append(sort.charAt(i));
			
			} else {
				fillSortValues(filterOrder, sortField.toString());
				filterOrders.add(filterOrder);
				
				filterOrder = new FilterOrder();
				sortField = new StringBuilder();
			}
		}
		
		fillSortValues(filterOrder, sortField.toString());
		filterOrders.add(filterOrder);
		
		return filterOrders;
	}

	/**
	 * Fill sort values from parameter to new entity of {@link FilterOrder}.
	 * 
	 * @param filterOrder
	 * @param sortField
	 */
	private static void fillSortValues(FilterOrder filterOrder, String sortField) {
		StringBuilder field = new StringBuilder();
		SortOrder sortOrder = DEFAULT_SORT_ORDER;
		
		for (int i = 0; i < sortField.length(); i++) {
			
			if (sortField.charAt(i) != '=') {
				field.append(sortField.charAt(i));
			
			} else {
				SortOrder parsedSortOrer = SortOrder.of(sortField.substring(i + 1, sortField.length()).trim());
				
				if (parsedSortOrer != null) {
					sortOrder = parsedSortOrer;
				}
				break;
			}
		}
		
		filterOrder.setField(field.toString().trim());
		filterOrder.setSortOrder(sortOrder);
	}

	/**
	 * Filter order toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "FilterOrder [field=" + field + ", sortOrder=" + sortOrder + "]";
	}
    
}