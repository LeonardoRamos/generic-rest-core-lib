package com.generic.rest.core.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

/**
 * Class responsible for mapping attributes related to an API aggregation and basic database entity.
 * 
 * @author leonardo.ramos
 *
 */
@MappedSuperclass
@JsonInclude(Include.NON_EMPTY)
public class BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	@Transient
	private Map<String, Object> sum = new HashMap<>();
	
	@Transient
	private Map<String, Object> avg = new HashMap<>();
	
	@Transient
	private Map<String, Object> count = new HashMap<>();
	
	/**
	 * Default constructor.
	 */
	public BaseEntity() {}

	/**
	 * Builder constructor.
	 * 
	 * @param builder
	 */
	public BaseEntity(BaseEntityBuilder builder) {
		this.id = builder.id;
		this.sum = builder.sum;
		this.avg = builder.avg;
		this.count = builder.count;
	}
	
	/**
	 * Return the id.
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Return the aggregation sum.
	 * 
	 * @return sum
	 */
	public Map<String, Object> getSum() {
		return sum;
	}

	/**
	 * Set the aggregation sum.
	 * 
	 * @param sum
	 */
	public void setSum(Map<String, Object> sum) {
		this.sum = sum;
	}
	
	/**
	 * Add a value entry to a key/nested key field.
	 * 
	 * @param sum
	 */
	@SuppressWarnings("unchecked")
	public void addSum(Map<String, Object> sum) {
		Map.Entry<String, Object> sumEntry = sum.entrySet().iterator().next();
		Map<String, Object> sumCurrent = this.sum;
		
		while (sumCurrent.get(sumEntry.getKey()) != null) {
			sumCurrent = (Map<String, Object>) sumCurrent.get(sumEntry.getKey());
			sum = (Map<String, Object>) sumEntry.getValue();
			sumEntry = sum.entrySet().iterator().next();
		}
		
		sumCurrent.put(sumEntry.getKey(), sumEntry.getValue());
	}

	/**
	 * Return the avg.
	 * 
	 * @return avg
	 */
	public Map<String, Object> getAvg() {
		return avg;
	}

	/**
	 * Set the avg.
	 * 
	 * @param avg
	 */
	public void setAvg(Map<String, Object> avg) {
		this.avg = avg;
	}
	
	/**
	 * Add a value entry to a key/nested key field.
	 * 
	 * @param avg
	 */
	@SuppressWarnings("unchecked")
	public void addAvg(Map<String, Object> avg) {
		Map.Entry<String, Object> avgEntry = avg.entrySet().iterator().next();
		Map<String, Object> avgCurrent = this.avg;
		
		while (avgCurrent.get(avgEntry.getKey()) != null) {
			avgCurrent = (Map<String, Object>) avgCurrent.get(avgEntry.getKey());
			avg = (Map<String, Object>) avgEntry.getValue();
			avgEntry = avg.entrySet().iterator().next();
		}
		
		avgCurrent.put(avgEntry.getKey(), avgEntry.getValue());
	}

	/**
	 * Return the count.
	 * 
	 * @return count
	 */
	public Map<String, Object> getCount() {
		return count;
	}

	/**
	 * Set the count.
	 * 
	 * @param count
	 */
	public void setCount(Map<String, Object> count) {
		this.count = count;
	}
	
	/**
	 * Add a value entry to a key/nested key field.
	 * 
	 * @param count
	 */
	@SuppressWarnings("unchecked")
	public void addCount(Map<String, Object> count) {
		Map.Entry<String, Object> countEntry = count.entrySet().iterator().next();
		Map<String, Object> countCurrent = this.count;
		
		while (countCurrent.get(countEntry.getKey()) != null) {
			countCurrent = (Map<String, Object>) countCurrent.get(countEntry.getKey());
			count = (Map<String, Object>) countEntry.getValue();
			countEntry = count.entrySet().iterator().next();
		}
		
		countCurrent.put(countEntry.getKey(), countEntry.getValue());
	}
	
	/**
	 * Builder pattern to build an instance of {@link BaseEntity}.
	 * 
	 * @return {@link BaseEntityBuilder}
	 */
	public static BaseEntityBuilder builder() {
		return new BaseEntityBuilder();
	}
	
	/**
	 * Builder pattern inner class to build a new instance of {@link BaseEntity}.
	 * 
	 * @author leonardo.ramos
	 *
	 */
	public static class BaseEntityBuilder {
		
		private Long id;
		private Map<String, Object> sum = new HashMap<>();
		private Map<String, Object> avg = new HashMap<>();
		private Map<String, Object> count = new HashMap<>();
		
		/**
		 * Set the id to builder.
		 * 
		 * @param id
		 * @return {@link BaseEntityBuilder}
		 */
		public BaseEntityBuilder id(Long id) {
			this.id = id;
			return this;
		}
		
		/**
		 * Set the sum to builder.
		 * 
		 * @param sum
		 * @return {@link BaseEntityBuilder}
		 */
		public BaseEntityBuilder sum(Map<String, Object> sum) {
			this.sum = sum;
			return this;
		}
		
		/**
		 * Set the avg to builder.
		 * 
		 * @param avg
		 * @return {@link BaseEntityBuilder}
		 */
		public BaseEntityBuilder avg(Map<String, Object> avg) {
			this.avg = avg;
			return this;
		}
		
		/**
		 * Set the count to builder.
		 * 
		 * @param count
		 * @return {@link BaseEntityBuilder}
		 */
		public BaseEntityBuilder count(Map<String, Object> count) {
			this.count = count;
			return this;
		}
		
		/**
		 * Return the id.
		 * 
		 * @return id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * Set the id.
		 * 
		 * @param id
		 */
		public void setId(Long id) {
			this.id = id;
		}
		
		/**
		 * Return the sum.
		 * 
		 * @return sum
		 */
		public Map<String, Object> getSum() {
			return sum;
		}

		/**
		 * Set the sum.
		 * 
		 * @param sum
		 */
		public void setSum(Map<String, Object> sum) {
			this.sum = sum;
		}

		/**
		 * Return the avg.
		 * 
		 * @return avg
		 */
		public Map<String, Object> getAvg() {
			return avg;
		}

		/**
		 * Set the avg.
		 * 
		 * @param avg
		 */
		public void setAvg(Map<String, Object> avg) {
			this.avg = avg;
		}

		/**
		 * Return the count.
		 * 
		 * @return count
		 */
		public Map<String, Object> getCount() {
			return count;
		}

		/**
		 * Set the count.
		 * 
		 * @param count
		 */
		public void setCount(Map<String, Object> count) {
			this.count = count;
		}
	}
	
}
