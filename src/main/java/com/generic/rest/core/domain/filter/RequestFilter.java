package com.generic.rest.core.domain.filter;

import java.util.Arrays;
import java.util.List;

import com.generic.rest.core.util.StringParserUtils;

import io.micrometer.common.util.StringUtils;

public class RequestFilter {
	
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
	public static final Integer DEFAULT_OFFSET = 0;
	public static final Integer DEFAULT_LIMIT = 20;
	public static final Integer MAX_LIMIT = 100;
	
	private String normalizeSymbol(String symbol) {
		if (symbol != null) {
			symbol = StringParserUtils.replace(StringParserUtils.replace(symbol, "[", ""), "]", "");
		}
		
		return symbol;
	}

	private String parseFilterOperators(String filter) {
		FilterOperator[] operators = new FilterOperator[] { FilterOperator.EQ, FilterOperator.GT, FilterOperator.LT };
		List<FilterOperator> simpleCharOperator = Arrays.asList(operators);
		
		for (FilterOperator filterOperator : FilterOperator.values()) {
			if (!simpleCharOperator.contains(filterOperator)) {
				filter = StringParserUtils.replace(filter, filterOperator.getOperatorCommonAlias(), filterOperator.getParseableOperator());
				filter = StringParserUtils.replace(filter, filterOperator.getOperatorAlias(), filterOperator.getParseableOperator());
			}
		}
		
		for (FilterOperator simpleCharfilterOperator : simpleCharOperator) {
			filter = StringParserUtils.replace(filter, simpleCharfilterOperator.getOperatorCommonAlias(), simpleCharfilterOperator.getParseableOperator());
			filter = StringParserUtils.replace(filter, simpleCharfilterOperator.getOperatorAlias(), simpleCharfilterOperator.getParseableOperator());
		}
		
		return filter;
	}
	
	public void addAndFilter(String filterName, Object filterValue, FilterOperator filterOperator) {
		if (StringUtils.isBlank(this.filter) || "[]".equals(this.filter)) {
			this.filter = new StringBuilder(filterName)
					.append(filterOperator.getParseableOperator())
					.append(filterValue.toString()).toString();
		} else {
			if (FilterOperator.IN.equals(filterOperator) || FilterOperator.OU.equals(filterOperator)) {
				this.filter = new StringBuilder(StringParserUtils.replace(StringParserUtils.replace(this.filter, "[", ""), "]", ""))
						.append(LogicOperator.AND.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append("(")
						.append(filterValue.toString())
						.append(")").toString();
			} else {
				this.filter = new StringBuilder(StringParserUtils.replace(StringParserUtils.replace(this.filter, "[", ""), "]", ""))
						.append(LogicOperator.AND.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append(filterValue.toString()).toString();
			}
		}
	}
	
	public void addCountField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addCountField(fieldName);
            }
        }
    }
    
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
    
    public void addCountDistinctField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addCountDistinctField(fieldName);
            }
        }
    }
    
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
    
    public void addSumField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addSumField(fieldName);
            }
        }
    }
    
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
    
    public void addAvgField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addAvgField(fieldName);
            }
        }
    }
    
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
    
    public void addGroupByField(List<String> fieldNames) {
        if (fieldNames != null && !fieldNames.isEmpty()) {
            for (String fieldName : fieldNames) {
                this.addGroupByField(fieldName);
            }
        }
    }
    
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

	public void addOrFilter(String filterName, String filterValue, FilterOperator filterOperator) {
		if (StringUtils.isBlank(this.filter) || "[]".equals(filter)) {
			this.filter = new StringBuilder(filterName)
					.append(filterOperator.getParseableOperator())
					.append(filterValue).toString();
		} else {
			if (FilterOperator.IN.equals(filterOperator) || FilterOperator.OU.equals(filterOperator)) {
				this.filter = new StringBuilder(StringParserUtils.replace(StringParserUtils.replace(this.filter, "[", ""), "]", ""))
						.append(LogicOperator.OR.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append("(")
						.append(filterValue)
						.append(")").toString();
			} else {
				this.filter = new StringBuilder(StringParserUtils.replace(StringParserUtils.replace(this.filter, "[", ""), "]", ""))
						.append(LogicOperator.OR.getOperator())
						.append(filterName)
						.append(filterOperator.getParseableOperator())
						.append(filterValue).toString();
			}
		}
	}
	
	public boolean hasValidAggregateFunction() {
		return StringUtils.isNotBlank(this.sum) || 
			   StringUtils.isNotBlank(this.avg) || 
			   StringUtils.isNotBlank(this.count) ||
			   StringUtils.isNotBlank(this.countDistinct);
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		if (filter != null) {
			filter = StringParserUtils.replace(StringParserUtils.replace(filter, "[", ""), "]", "");
			filter = StringParserUtils.replace(filter, LogicOperator.AND.getOperatorAlias(), LogicOperator.AND.getOperator());
			filter = StringParserUtils.replace(filter, LogicOperator.OR.getOperatorAlias(), LogicOperator.OR.getOperator());
			
			filter = this.parseFilterOperators(filter);
		}
		this.filter = filter;
	}

	public String getProjection() {
		return projection;
	}
	
	public void setProjection(String projection) {
		if (projection != null) {
			projection = this.normalizeSymbol(projection);
		}
		this.projection = projection;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		if (sort != null) {
			sort = this.normalizeSymbol(sort);
		}
		this.sort = sort;
	}

	public String getOffset() {
		return offset;
	}
	
	public Integer getFetchOffset() {
		if (offset == null || "".equals(offset)) {
			return DEFAULT_OFFSET;
		}
		
		return Integer.parseInt(offset);
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
	
	public void setOffset(Integer offset) {
		if (offset != null) {
			this.offset = offset.toString();
		}
	}

	public String getLimit() {
		return limit;
	}
	
	public Integer getFetchLimit() {
		if (StringUtils.isBlank(this.limit)) {
			return DEFAULT_LIMIT;
		}
		
		Integer fetchLimit = Integer.parseInt(this.limit);
		
		return fetchLimit <= MAX_LIMIT ? fetchLimit : MAX_LIMIT;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public void setLimit(Integer limit) {
		if (limit != null) {
			this.limit = limit.toString();
		}
	}

	public String getSum() {
		return sum;
	}
	
	public void setSum(String sum) {
		if (sum != null) {
			sum = this.normalizeSymbol(sum);
		}
		this.sum = sum;
	}
	
	public String getAvg() {
		return avg;
	}
	
	public void setAvg(String avg) {
		if (avg != null) {
			avg = this.normalizeSymbol(avg);
		}
		this.avg = avg;
	}

	public String getGroupBy() {
		return groupBy;
	}
	
	public void setGroupBy(String groupBy) {
		if (groupBy != null) {
			groupBy = this.normalizeSymbol(groupBy);
		}
		this.groupBy = groupBy;
	}
	
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		if (count != null) {
			count = this.normalizeSymbol(count);
		}
		this.count = count;
	}
	
	public String getCountDistinct() {
		return countDistinct;
	}
	
	public void setCountDistinct(String countDistinct) {
		if (countDistinct != null) {
			countDistinct = this.normalizeSymbol(countDistinct);
		}
		this.countDistinct = countDistinct;
	}
	
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
	
	private String getRawValue(String value) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		return value;
	}

	@Override
	public String toString() {
		return "RequestFilter [filter=" + filter + ", projection=" + projection + ", sum=" + sum + ", avg=" + avg
				+ ", count=" + count + ", countDistinct=" + countDistinct + ", groupBy=" + groupBy + ", sort=" + sort
				+ ", offset=" + offset + ", limit=" + limit + "]";
	}

}