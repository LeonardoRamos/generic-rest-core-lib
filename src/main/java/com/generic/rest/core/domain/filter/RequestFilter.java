package com.generic.rest.core.domain.filter;

import java.util.Arrays;
import java.util.List;

import com.generic.rest.core.util.StringParserUtils;

import io.micrometer.common.util.StringUtils;

/**
 * Class responsible for having all filters and aggregations related to an API query.
 * 
 * @author leonardo.ramos
 *
 */
public class RequestFilter {
	
	public static final Integer DEFAULT_OFFSET = 0;
	public static final Integer DEFAULT_LIMIT = 20;
	public static final Integer MAX_LIMIT = 100;

	private String filter;
	private String projection;
	private String sum;
	private String avg;
	private String count;
	private String countDistinct;
	private String groupBy;
	private String sort; 
	private String offset;
	private String limit;
	
	/**
	 * Add {@link LogicOperator#AND} operator in query filter.
	 * 
	 * @param filterName
	 * @param filterValue
	 * @param filterOperator
	 */
	public void addAndFilter(String filterName, Object filterValue, FilterOperator filterOperator) {
		if (StringUtils.isBlank(this.filter) || "[]".equals(this.filter)) {
			this.filter = new StringBuilder(filterName)
					.append(filterOperator.getParseableOperator())
					.append(filterValue.toString()).toString();
		} else {
			if (FilterOperator.IN.equals(filterOperator) || FilterOperator.OU.equals(filterOperator)) {
				this.filter = new StringBuilder(StringParserUtils.replace(this.filter, new String[]{"[", "]"}, ""))
						.append(LogicOperator.AND.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append("(")
						.append(filterValue.toString())
						.append(")").toString();
			} else {
				this.filter = new StringBuilder(StringParserUtils.replace(this.filter, new String[]{"[", "]"}, ""))
						.append(LogicOperator.AND.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append(filterValue.toString()).toString();
			}
		}
	}
	
	/**
	 * Add {@link LogicOperator#OR} operator in query filter.
	 * 
	 * @param filterName
	 * @param filterValue
	 * @param filterOperator
	 */
	public void addOrFilter(String filterName, String filterValue, FilterOperator filterOperator) {
		if (StringUtils.isBlank(this.filter) || "[]".equals(filter)) {
			this.filter = new StringBuilder(filterName)
					.append(filterOperator.getParseableOperator())
					.append(filterValue).toString();
		} else {
			if (FilterOperator.IN.equals(filterOperator) || FilterOperator.OU.equals(filterOperator)) {
				this.filter = new StringBuilder(StringParserUtils.replace(this.filter, new String[]{"[", "]"}, ""))
						.append(LogicOperator.OR.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append("(")
						.append(filterValue)
						.append(")").toString();
			} else {
				this.filter = new StringBuilder(StringParserUtils.replace(this.filter, new String[]{"[", "]"}, ""))
						.append(LogicOperator.OR.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append(filterValue).toString();
			}
		}
	}
	
	/**
	 * Add count fields to filter params.
	 * 
	 * @param fieldNames
	 */
	public void addCountField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addCountField(fieldName);
            }
        }
    }
    
	/**
	 * Add count field to filter params.
	 * 
	 * @param fieldName
	 */
    public void addCountField(String fieldName) {
        StringBuilder countFields = new StringBuilder();
        if (StringUtils.isBlank(this.count)) {
        	this.count = countFields.append("[")
                .append(fieldName)
                .append("]")
                .toString();
        
        } else {
        	this.count = countFields.append(StringParserUtils.replace(this.count, "]", ","))
                .append(fieldName)
                .append("]")
                .toString();
        }
    }
    
    /**
	 * Add count distinct fields to filter params.
	 * 
	 * @param fieldNames
	 */
    public void addCountDistinctField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addCountDistinctField(fieldName);
            }
        }
    }
    
    /**
	 * Add count distinct field to filter params.
	 * 
	 * @param fieldName
	 */
    public void addCountDistinctField(String fieldName) {
        StringBuilder countDistinctFields = new StringBuilder();
        if (this.countDistinct == null || "".equals(this.countDistinct)) {
        	this.countDistinct = countDistinctFields.append("[")
                .append(fieldName)
                .append("]")
                .toString();
        
        } else {
        	this.countDistinct = countDistinctFields.append(StringParserUtils.replace(this.countDistinct, "]", ","))
                .append(fieldName)
                .append("]")
                .toString();
        }
    }
    
    /**
	 * Add sum fields to filter params.
	 * 
	 * @param fieldNames
	 */
    public void addSumField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addSumField(fieldName);
            }
        }
    }
    
    /**
	 * Add sum field to filter params.
	 * 
	 * @param fieldName
	 */
    public void addSumField(String fieldName) {
        StringBuilder sumFields = new StringBuilder();
        if (StringUtils.isBlank(this.sum)) {
        	this.countDistinct = sumFields.append("[")
                .append(fieldName)
                .append("]")
                .toString();
        
        } else {
        	this.sum = sumFields.append(StringParserUtils.replace(this.sum, "]", ","))
                .append(fieldName)
                .append("]")
                .toString();
        }
    }
    
    /**
	 * Add avg fields to filter params.
	 * 
	 * @param fieldNames
	 */
    public void addAvgField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addAvgField(fieldName);
            }
        }
    }
    
    /**
	 * Add avg field to filter params.
	 * 
	 * @param fieldName
	 */
    public void addAvgField(String fieldName) {
        StringBuilder avgFields = new StringBuilder();
        if (StringUtils.isBlank(this.avg)) {
        	this.countDistinct = avgFields.append("[")
                .append(fieldName)
                .append("]")
                .toString();
        
        } else {
        	this.avg = avgFields.append(StringParserUtils.replace(this.avg, "]", ","))
                .append(fieldName)
                .append("]")
                .toString();
        }
    }
    
    /**
	 * Add group by fields to filter params.
	 * 
	 * @param fieldNames
	 */
    public void addGroupByField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addGroupByField(fieldName);
            }
        }
    }
    
    /**
	 * Add group by field to filter params.
	 * 
	 * @param fieldName
	 */
    public void addGroupByField(String fieldName) {
        StringBuilder groupByFields = new StringBuilder();
        if (StringUtils.isBlank(this.groupBy)) {
        	this.groupBy = groupByFields.append("[")
                .append(fieldName)
                .append("]")
                .toString();
        
        } else {
        	this.groupBy = groupByFields.append(StringParserUtils.replace(this.groupBy, "]", ","))
                .append(fieldName)
                .append("]")
                .toString();
        }
    }
    
    /**
	 * Add sort field to filter params.
	 * 
	 * @param fieldName
	 * @param sortOrder
	 */
    public void addSortField(String fieldName, SortOrder sortOrder) {
        StringBuilder sortFields = new StringBuilder();
        if (StringUtils.isBlank(this.sort)) {
        	this.sort = sortFields.append("[")
                .append(fieldName)
                .append("=")
                .append(sortOrder.name())
                .append("]")
                .toString();
        
        } else {
        	this.sort = sortFields.append(StringParserUtils.replace(this.sort, "]", ","))
                .append(fieldName)
                .append("=")
                .append(sortOrder.name())
                .append("]")
                .toString();
        }
    }

	/**
	 * Verify if filter has a valid aggregation.
	 * 
	 * @return true if filter has valid aggregation false otherwise
	 */
	public boolean hasValidAggregateFunction() {
		return StringUtils.isNotBlank(this.sum) || 
			   StringUtils.isNotBlank(this.avg) || 
			   StringUtils.isNotBlank(this.count) ||
			   StringUtils.isNotBlank(this.countDistinct);
	}
	
	/**
	 * Return the filter.
	 * 
	 * @return filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the filter.
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		if (filter != null) {
			filter = StringParserUtils.replace(filter, new String[] {"[", "]"}, "");
			filter = StringParserUtils.replace(filter, LogicOperator.AND.getOperatorAlias(), LogicOperator.AND.getOperator());
			filter = StringParserUtils.replace(filter, LogicOperator.OR.getOperatorAlias(), LogicOperator.OR.getOperator());
			
			filter = this.parseFilterOperators(filter);
		}
		this.filter = filter;
	}
	
	/**
	 * Normalize and parse the operators of a given text filter. </p>
	 * Parse simple char Operators like '<', '>' and '=' for later because this special characters are within other operators like '>=' for example

	 * 
	 * @param filter
	 * @return parsed and normalized filter operator text
	 */
	private String parseFilterOperators(String filter) {
		FilterOperator[] operators = new FilterOperator[] { FilterOperator.EQ, FilterOperator.GT, FilterOperator.LT };
		List<FilterOperator> simpleCharOperator = Arrays.asList(operators);
		
		for (FilterOperator filterOperator : FilterOperator.values()) {
			if (!simpleCharOperator.contains(filterOperator)) {
				filter = StringParserUtils.replace(filter, 
						new String[] { filterOperator.getOperatorCommonAlias(), filterOperator.getOperatorAlias() }, 
						filterOperator.getParseableOperator());
			}
		}
		
		for (FilterOperator simpleCharfilterOperator : simpleCharOperator) {
			filter = StringParserUtils.replace(filter, 
					new String[] { simpleCharfilterOperator.getOperatorCommonAlias(), simpleCharfilterOperator.getOperatorAlias() }, 
					simpleCharfilterOperator.getParseableOperator());
		}
		
		return filter;
	}

	/**
	 * Return the projection.
	 * 
	 * @return projection
	 */
	public String getProjection() {
		return projection;
	}
	
	/**
	 * Set the projection.
	 * 
	 * @param projection
	 */
	public void setProjection(String projection) {
		if (projection != null) {
			projection = this.normalizeParam(projection);
		}
		this.projection = projection;
	}

	/**
	 * Return the query sort.
	 * 
	 * @return sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * Set the query sort.
	 * 
	 * @param sort
	 */
	public void setSort(String sort) {
		if (sort != null) {
			sort = this.normalizeParam(sort);
		}
		this.sort = sort;
	}

	/**
	 * Return the offset parameter.
	 * 
	 * @return offset
	 */
	public String getOffset() {
		return offset;
	}
	
	/**
	 * Return the offset for fetching operation.
	 * 
	 * @return offset
	 */
	public Integer getFetchOffset() {
		if (offset == null || "".equals(offset)) {
			return DEFAULT_OFFSET;
		}
		
		return Integer.parseInt(offset);
	}

	/**
	 * Set the offset.
	 * 
	 * @param offset
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}
	
	/**
	 * Set the offset from Integer value.
	 * 
	 * @param offset
	 */
	public void setOffset(Integer offset) {
		if (offset != null) {
			this.offset = offset.toString();
		}
	}

	/**
	 * Return the limit parameter.
	 * 
	 * @return limit
	 */
	public String getLimit() {
		return limit;
	}
	
	/**
	 * Return the limit for fetching operation. 
	 * 
	 * @return limit
	 */
	public Integer getFetchLimit() {
		if (StringUtils.isBlank(this.limit)) {
			return DEFAULT_LIMIT;
		}
		
		Integer fetchLimit = Integer.parseInt(this.limit);
		
		return fetchLimit <= MAX_LIMIT ? fetchLimit : MAX_LIMIT;
	}

	/**
	 * Set limit.
	 * 
	 * @param limit
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	/**
	 * Set limit from Integer value.
	 * 
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		if (limit != null) {
			this.limit = limit.toString();
		}
	}

	/**
	 * Return sum.
	 * 
	 * @return sum
	 */
	public String getSum() {
		return sum;
	}
	
	/**
	 * Set sum.
	 * 
	 * @param sum
	 */
	public void setSum(String sum) {
		if (sum != null) {
			sum = this.normalizeParam(sum);
		}
		this.sum = sum;
	}
	
	/**
	 * Return avg.
	 * 
	 * @return avg
	 */
	public String getAvg() {
		return avg;
	}
	
	/**
	 * Set avg.
	 * 
	 * @param avg
	 */
	public void setAvg(String avg) {
		if (avg != null) {
			avg = this.normalizeParam(avg);
		}
		this.avg = avg;
	}

	/**
	 * Return group by.
	 * 
	 * @return groupBy
	 */
	public String getGroupBy() {
		return groupBy;
	}
	
	/**
	 * Set group by.
	 * 
	 * @param groupBy
	 */
	public void setGroupBy(String groupBy) {
		if (groupBy != null) {
			groupBy = this.normalizeParam(groupBy);
		}
		this.groupBy = groupBy;
	}
	
	/**
	 * Return count.
	 * 
	 * @return count
	 */
	public String getCount() {
		return count;
	}
	
	/**
	 * Set count.
	 * 
	 * @param count
	 */
	public void setCount(String count) {
		if (count != null) {
			count = this.normalizeParam(count);
		}
		this.count = count;
	}
	
	/**
	 * Return count distinct.
	 * 
	 * @return countDistinct
	 */
	public String getCountDistinct() {
		return countDistinct;
	}
	
	/**
	 * Set count distinct.
	 * 
	 * @param countDistinct
	 */
	public void setCountDistinct(String countDistinct) {
		if (countDistinct != null) {
			countDistinct = this.normalizeParam(countDistinct);
		}
		this.countDistinct = countDistinct;
	}
	
	/**
	 * Normalize parameter removing special characters '[' and ']'.
	 * 
	 * @param symbol
	 * @return normalized param
	 */
	private String normalizeParam(String symbol) {
		if (symbol != null) {
			symbol = StringParserUtils.replace(symbol, new String[]{"[", "]"}, "");
		}
		
		return symbol;
	}
	
	/**
	 * return raw string with parameters from request filter. It's essentially used as key for cache.
	 * 
	 * @return raw request filter
	 */
	public String getRawRequestFilter() {
		StringBuilder rawFilter = new StringBuilder();
		
		rawFilter.append("filter=[").append(this.getRawValue(this.filter)).append("],");
		rawFilter.append("projection=[").append(this.getRawValue(this.projection)).append("],");
		rawFilter.append("sum=[").append(this.getRawValue(this.sum)).append("],");
		rawFilter.append("avg=[").append(this.getRawValue(this.avg)).append("],");
		rawFilter.append("count=[").append(this.getRawValue(this.count)).append("],");
		rawFilter.append("countDistinct=[").append(this.getRawValue(this.countDistinct)).append("],");
		rawFilter.append("groupBy=[").append(this.getRawValue(this.groupBy)).append("],");
		rawFilter.append("sort=[").append(this.getRawValue(this.sort)).append("],");
		rawFilter.append("offset=").append(this.getFetchOffset());
		rawFilter.append("limit=").append(this.getFetchLimit());
		
		return rawFilter.toString();
	}
	
	/**
	 * Return raw value of filter URL or an empty String in case the param is not present.
	 * 
	 * @param value
	 * @return raw filter param value
	 */
	private String getRawValue(String value) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		return value;
	}

	/**
	 * Request filter toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "RequestFilter [filter=" + filter + ", projection=" + projection + ", sum=" + sum + ", avg=" + avg
				+ ", count=" + count + ", countDistinct=" + countDistinct + ", groupBy=" + groupBy + ", sort=" + sort
				+ ", offset=" + offset + ", limit=" + limit + "]";
	}

}