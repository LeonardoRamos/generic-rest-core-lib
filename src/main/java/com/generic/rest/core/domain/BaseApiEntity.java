package com.generic.rest.core.domain;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Class responsible for mapping attributes related to an API entity.
 * 
 * @author leonardo.ramos
 *
 */
@MappedSuperclass
@JsonInclude(Include.NON_EMPTY)
public class BaseApiEntity extends BaseEntity {
	
	@Column(name = "externalId", nullable = false, updatable = false, length = 32)
	private String externalId;
	
	@Column(name = "active", nullable = false)
	private boolean active;
	
	@Column(name = "creation_date", nullable = false, updatable = false)
	private Calendar insertDate;
	
	@Column(name = "update_date", nullable = false)
	private Calendar updateDate;
	
	@Column(name = "delete_date")
	private Calendar deleteDate;
	
	/**
	 * Default constructor.
	 */
	public BaseApiEntity() {}

	/**
	 * Builder constructor.
	 * 
	 * @param builder
	 */
	public BaseApiEntity(BaseApiEntityBuilder builder) {
		this.externalId = builder.externalId;
		this.active = builder.active;
		this.insertDate = builder.insertDate;
		this.updateDate = builder.updateDate;
		this.deleteDate = builder.removeDate;
		this.setId(builder.getId());
		this.setSum(builder.getSum());
		this.setAvg(builder.getAvg());
		this.setCount(builder.getCount());
	}
	
	/**
	 * Return the id.
	 */
	@Override
	@JsonIgnore
	public Long getId() {
		return super.getId();
	}
	
	/**
	 * Set the externalId.
	 * 
	 * @return externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Set the externalId.
	 * 
	 * @param externalId
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Return if entity is active.
	 * 
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set active.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Return the insertDate.
	 * 
	 * @return insertDate
	 */
	public Calendar getInsertDate() {
		return insertDate;
	}

	/**
	 * Set the insertDate.
	 * 
	 * @param insertDate
	 */
	public void setInsertDate(Calendar insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * Return the updateDate.
	 * 
	 * @return updateDate
	 */
	public Calendar getUpdateDate() {
		return updateDate;
	}

	/**
	 * Set the updateDate.
	 * 
	 * @param updateDate
	 */
	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Return the deleteDate.
	 * 
	 * @return deleteDate
	 */
	public Calendar getDeleteDate() {
		return deleteDate;
	}

	/**
	 * Set the deleteDate.
	 * 
	 * @param deleteDate
	 */
	public void setDeleteDate(Calendar deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	/**
	 * Builder pattern to build an instance of {@link BaseApiEntity}.
	 * 
	 * @return {@link BaseApiEntityBuilder}
	 */
	public static BaseApiEntityBuilder builder() {
		return new BaseApiEntityBuilder();
	}
	
	/**
	 * Builder pattern inner class to build a new instance of {@link BaseApiEntity}.
	 * 
	 * @author leonardo.ramos
	 *
	 */
	public static class BaseApiEntityBuilder extends BaseEntityBuilder {
		
		private String externalId;
		private boolean active = true;
		private Calendar insertDate;
		private Calendar updateDate;
		private Calendar removeDate;

		/**
		 * Set the externalId to builder.
		 * 
		 * @param externalId
		 * @return {@link BaseApiEntityBuilder}
		 */
		public BaseApiEntityBuilder externalId(String externalId) {
			this.externalId = externalId;
			return this;
		}
		
		/**
		 * Set the active to builder.
		 * 
		 * @param active
		 * @return {@link BaseApiEntityBuilder}
		 */
		public BaseApiEntityBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		 * Set the insertDate to builder.
		 * 
		 * @param insertDate
		 * @return {@link BaseApiEntityBuilder}
		 */
		public BaseApiEntityBuilder insertDate(Calendar insertDate) {
			this.insertDate = insertDate;
			return this;
		}
		
		/**
		 * Set the updateDate to builder.
		 * 
		 * @param updateDate
		 * @return {@link BaseApiEntityBuilder}
		 */
		public BaseApiEntityBuilder updateDate(Calendar updateDate) {
			this.updateDate = updateDate;
			return this;
		}
		
		/**
		 * Set the removeDate to builder.
		 * 
		 * @param removeDate
		 * @return {@link BaseApiEntityBuilder}
		 */
		public BaseApiEntityBuilder removeDate(Calendar removeDate) {
			this.removeDate = removeDate;
			return this;
		}
		
		/**
		 * Set the externalId.
		 * 
		 * @return externalId
		 */
		public String getExternalId() {
			return externalId;
		}

		/**
		 * Set the externalId.
		 * 
		 * @param externalId
		 */
		public void setExternalId(String externalId) {
			this.externalId = externalId;
		}

		/**
		 * Return if entity is active.
		 * 
		 * @return active
		 */
		public boolean isActive() {
			return active;
		}

		/**
		 * Set active.
		 * 
		 * @param active
		 */
		public void setActive(boolean active) {
			this.active = active;
		}

		/**
		 * Return the insertDate. 
		 * 
		 * @return insertDate
		 */
		public Calendar getInsertDate() {
			return insertDate;
		}

		/**
		 * Set the insertDate.
		 * 
		 * @param insertDate
		 */
		public void setInsertDate(Calendar insertDate) {
			this.insertDate = insertDate;
		}

		/**
		 * Return the updateDate.
		 * 
		 * @return updateDate
		 */
		public Calendar getUpdateDate() {
			return updateDate;
		}

		/**
		 * Set the updateDate.
		 * 
		 * @param updateDate
		 */
		public void setUpdateDate(Calendar updateDate) {
			this.updateDate = updateDate;
		}

		/**
		 * Return the removeDate.
		 * 
		 * @return removeDate
		 */
		public Calendar getRemoveDate() {
			return removeDate;
		}

		/**
		 * Set the removeDate.
		 * 
		 * @param removeDate
		 */
		public void setRemoveDate(Calendar removeDate) {
			this.removeDate = removeDate;
		}
	}
	
}
