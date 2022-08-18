package com.gcgenome.lims.data;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Analysis {
	private String batch;
	private Double row;
	private Request request;
	private String sample;
	private Double freemix;
	@JsProperty(name="raw_reads_millions")
	private Double rawReadsMillions;
	@JsProperty(name="dup_rate")
	private Double dupRate;
	private Double gc;
	@JsProperty(name="raw_reads")
	private Double rawReads;
	@JsProperty(name="total_reads")
	private Double totalReads;
	private Double mean;
	private Double median;
	private String qc;
	@JsProperty(name="cad_ensemble_prob")
	private Double cadEnsembleProb;
	@JsProperty(name="freemix_tmp")
	private Double freemixTmp;
	@JsProperty(name="raw_reads_millions_tmp")
	private Double rawReadsMillionsTmp;
	@JsProperty(name="dup_rate_tmp")
	private Double dupRateTmp;
	@JsProperty(name="gc_tmp")
	private Double gcTmp;
	@JsProperty(name="raw_reads_tmp")
	private Long rawReadsTmp;
	@JsProperty(name="total_reads_tmp")
	private Double totalReadsTmp;
	@JsProperty(name="mean_tmp")
	private Double meanTmp;
	@JsProperty(name="median_tmp")
	private Double medianTmp;
	@JsProperty(name="qc_tmp")
	private String qcTmp;
	@JsProperty(name="cad_ensemble_prob_tmp")
	private Double cadEnsembleProbTmp;
	@JsProperty(name="too5_pred")
	private String too5Pred;
	@JsProperty(name="too5_fems_prob")
	private Double too5FemsProb;
	@JsProperty(name="too6_pred")
	private String too6Pred;
	@JsProperty(name="too6_fems_prob")
	private Double too6FemsProb;
	private Double iscore;
	private String result;
	@JsProperty(name="created_at")
	private String createAt;
	@JsProperty(name="last_modify_at")
	private String lastModifyAt;
	private User lastModifyBy;
	private String etc;
	private Report report;
	@JsOverlay
	@JsIgnore
	public Integer row() {
		if(row == null) return null;
		else return row.intValue();
	}
	@JsOverlay
	@JsIgnore
	public Request request() {
		if(request == null) return null;
		return Js.uncheckedCast(request);
	}
}
