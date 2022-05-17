package com.greencross.lims.entity;

import com.greencross.lims.entity.readonly.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="medicheck", name = "report")
@Data
@Accessors(fluent = true)
@NoArgsConstructor
public class Report {
	@EmbeddedId
	private ReportPK pk;
	@Setter(AccessLevel.NONE)
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="create_by", referencedColumnName="id")
	private User createBy;
	@Setter(AccessLevel.NONE)
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@Setter(AccessLevel.NONE)
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="last_modify_by")
	private User lastModifiedBy;
	@Column(name="publish_at")
	private LocalDateTime publishAt;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="publish_by")
	private User publishBy;
	@Column(name="file")
	private UUID file;
	@Column(name="name")
	private String name;
	@Column(name="size")
	private int size;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
		@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Analysis analysis;
	public LocalDateTime createAt() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(pk().createAt), ZoneId.systemDefault());
	}
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class ReportPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="create_at", nullable=false, updatable=false)
		private long createAt;
		public ReportPK() {}
		@Builder
		public ReportPK(Long sample, String service, long createAt) {
			this.sample = sample;
			this.service = service;
			this.createAt = createAt;
		}
	}
}
