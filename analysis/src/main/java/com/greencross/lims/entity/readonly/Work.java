package com.greencross.lims.entity.readonly;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(schema="public", name = "work")
@Getter
@Accessors(fluent = true)
public class Work {
	@EmbeddedId
	private WorkPK pk;
	@Column(name="serial", length=13)
	private String serial;
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false)
			, @JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)})
	private Request request;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class WorkPK implements Serializable {
		@Column(name="sheet", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="worklist", columnDefinition="integer", nullable=false, updatable=false)
		private Integer work;
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;

		public WorkPK() {}

		@Builder
		public WorkPK(UUID sheet, Integer work, Long sample, String service) {
			this.sheet = sheet;
			this.work = work;
			this.sample = sample;
			this.service = service;
		}
	}
}
