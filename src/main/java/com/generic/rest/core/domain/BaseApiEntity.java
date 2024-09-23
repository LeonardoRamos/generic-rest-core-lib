package com.generic.rest.core.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	
	public BaseApiEntity() {}

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
	
	@Override
	@JsonIgnore
	public Long getId() {
		return super.getId();
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Calendar getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Calendar insertDate) {
		this.insertDate = insertDate;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	public Calendar getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Calendar deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	public static BaseApiEntityBuilder builder() {
		return new BaseApiEntityBuilder();
	}
	
	public static class BaseApiEntityBuilder extends BaseEntityBuilder {
		
		private String externalId;
		private boolean active = true;
		private Calendar insertDate;
		private Calendar updateDate;
		private Calendar removeDate;

		public BaseApiEntityBuilder externalId(String externalId) {
			this.externalId = externalId;
			return this;
		}
		
		public BaseApiEntityBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		public BaseApiEntityBuilder insertDate(Calendar insertDate) {
			this.insertDate = insertDate;
			return this;
		}
		
		public BaseApiEntityBuilder updateDate(Calendar updateDate) {
			this.updateDate = updateDate;
			return this;
		}
		
		public BaseApiEntityBuilder removeDate(Calendar removeDate) {
			this.removeDate = removeDate;
			return this;
		}
		
		public String getExternalId() {
			return externalId;
		}

		public void setExternalId(String externalId) {
			this.externalId = externalId;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public Calendar getInsertDate() {
			return insertDate;
		}

		public void setInsertDate(Calendar insertDate) {
			this.insertDate = insertDate;
		}

		public Calendar getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Calendar updateDate) {
			this.updateDate = updateDate;
		}

		public Calendar getRemoveDate() {
			return removeDate;
		}

		public void setRemoveDate(Calendar removeDate) {
			this.removeDate = removeDate;
		}
	}
	
}
