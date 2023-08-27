package com.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
		value = { "createdAt", "updatedAt" },
		allowGetters = true
)
public abstract class AuditEntity {
	
	@CreatedDate
	@Column(name = "created_at", nullable = false,  updatable = false)
	private Instant createdAt;
	
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Integer createdBy;
	
	@LastModifiedDate
	@Column(name = "updated_at",  nullable = false)
	private Instant updatedAt;
	
	@LastModifiedBy
	@Column(name = "updated_by")
	private Integer updated_by;
	

}
