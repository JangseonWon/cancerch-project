package com.greencross.lims.entity.readonly;

import com.greencross.lims.entity.Analysis;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema="public", name = "request")
@Getter
@Accessors(fluent = true)
public class Request implements Serializable {
	@EmbeddedId
	private RequestPK pk;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id", insertable=false, updatable=false)
	private Service service;
	@Column(name="date_request")
	private LocalDate dateRequest;
	@Column(name="date_start")
	private LocalDate dateStart;
	@Column(name="date_due")
	private LocalDate dateDue;
	@Column(name="date_sampling")
	private LocalDate dateSampling;
	@Column(name="tat")
	private Integer tat;
	@Column(name="register")
	private Boolean registered = false;
	@Column(name="cancel")
	private Boolean canceled = false;
	@Column(name="delete")
	private Boolean deleted = false;
	@Formula("(CASE" +
					 " WHEN delete THEN '검사삭제'" +
					 " WHEN cancel THEN '검사취소'" +
					 " WHEN EXISTS (SELECT 1 FROM medicheck.report R WHERE R.sample=sample AND R.service=service AND R.publish_at IS NOT NULL) THEN '결과전송완료'" +
					 " WHEN EXISTS (SELECT 1 FROM medicheck.report R WHERE R.sample=sample AND R.service=service) THEN '결과지생성완료'" +
					 " WHEN EXISTS (SELECT 1 FROM medicheck.Analysis A WHERE A.sample=sample AND A.service=service) THEN '분석완료'" +
					 " ELSE '검사의뢰'" +
					 " END)")
	private String state;
	@Formula("(CASE WHEN EXISTS (SELECT 1 FROM medicheck.analysis A WHERE A.sample=sample AND A.service=service) THEN true ELSE false END)")
	private boolean analyzed;
	@Formula("(SELECT W.serial FROM public.work W WHERE W.sample=sample AND W.service=service ORDER BY W.create_time DESC LIMIT 1)")
	private String serial;
	@OneToMany
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable = false, updatable = false),
			@JoinColumn(name="service", referencedColumnName="service", insertable = false, updatable = false)})
	private List<Analysis> analysis;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class RequestPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;

		public RequestPK() {}

		@Builder
		public RequestPK(Long sample, String service) {
			this.sample = sample;
			this.service = service;
		}
	}
}
