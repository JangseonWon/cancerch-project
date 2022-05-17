package com.greencross.lims.entity.readonly;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="public", name = "sample")
@Data
@Accessors(fluent = true)
public class Sample {
	@Id
	@Column
	private long id;	// Sample Barcode ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient", referencedColumnName = "id")
	private Patient patient;
	@Column(name="sample_type", length=32, columnDefinition="varchar(32)")
	private String sampleType;
	@Column(name="remark", length=64, columnDefinition="varchar(64)")
	private String remark;
	@Column(name="barcode", columnDefinition="bigint")
	private Long barcode;

	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sample")
	private List<Request> requests;
}
