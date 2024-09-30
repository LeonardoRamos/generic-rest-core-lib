package com.generic.rest.core.domain;

import java.util.List;

/**
 * Entity responsible for storing API response data records and metadata for given query result.
 * 
 * @author leonardo.ramos
 *
 */
public class ApiResponse<E extends BaseEntity> {
	
	private List<E> records;
	private ApiMetadata metadata;
	
	/**
	 * Return the records.
	 * 
	 * @return records
	 */
	public List<E> getRecords() {
		return records;
	}

	/**
	 * Set the records.
	 * 
	 * @param records
	 */
	public void setRecords(List<E> records) {
		this.records = records;
	}

	/**
	 * Return API metadata.
	 * 
	 * @return metadata
	 */
	public ApiMetadata getMetadata() {
		return metadata;
	}

	/**
	 * Set the metadata.
	 * 
	 * @param metadata
	 */
	public void setMetadata(ApiMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * API response toString.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		return "ApiResponse [records=" + records + ", metadata=" + metadata + "]";
	}
	
}