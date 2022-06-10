package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(schema="avoid", name = "analysis", indexes = {
	@Index(columnList="sample")
}) @Data
@Accessors(fluent = true)
public class Analysis {
	@EmbeddedId
	@Setter(AccessLevel.PRIVATE)
	private AnalysisPK pk;
	@Setter(AccessLevel.NONE)
	@Column(name="sample", insertable = false, updatable = false)
	private Long sample;
	@Setter(AccessLevel.NONE)
	@Column(name="service", insertable = false, updatable = false)
	private String service;
	@Column(name="batch")
	private String batch;
	@Column(name="row")
	private Integer row;
	@Setter(AccessLevel.NONE)
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createAt = LocalDateTime.now();
	@Setter(AccessLevel.NONE)
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@Column(name="serial", length=64)
	private String serial;
	@Column(name="file")
	private String file;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter2")
	private Map<String, Object> value;
	@Column(name="etc")
	@Lob
	private String etc;
	protected Analysis(){}
	public Analysis(AnalysisPK pk) {
		this.pk = pk;
	}
	public Map<String, Object> value() {
		if(this.value==null) this.value = new HashMap<>();
		return this.value;
	}
	@Builder
	@Embeddable
	@Getter
	@EqualsAndHashCode
	@Accessors(fluent = true)
	public static class AnalysisPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private long sample;
		@Column(name="service", nullable=false, updatable=false)
		private String service;
		protected AnalysisPK(){}
		public AnalysisPK(long sample, String service) {
			this.sample = sample;
			this.service = service;
		}
	}
}
