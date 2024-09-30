package com.generic.rest.core.domain;

/**
 * Entity responsible for storing API response metadata for given results.
 * 
 * @author leonardo.ramos
 *
 */
public class ApiMetadata {
	
	private Long totalCount;
	private Integer pageOffset;
	private Integer pageSize;
	
	/**
	 * Return the total count.
	 * 
	 * @return the total count
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * Set total count.
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Return the page offset.
	 * 
	 * @return the page offset
	 */
	public Integer getPageOffset() {
		return pageOffset;
	}

	/**
	 * Set the page offset.
	 * 
	 * @param pageOffset
	 */
	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	/**
	 * Return the page size.
	 * 
	 * @return the page size
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Set the page size.
	 * 
	 * @param pageSize
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Metadata toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "ApiMetadata [totalCount=" + totalCount + ", pageOffset=" + pageOffset + ", pageSize=" + pageSize + "]";
	}
	
}