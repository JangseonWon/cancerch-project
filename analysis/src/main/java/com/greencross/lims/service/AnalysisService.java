package com.greencross.lims.service;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.readonly.Request;
import com.greencross.lims.mapper.AnalysisMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {
	private final AnalysisDao dao;
	private final RequestDao requestDao;
	private final AnalysisMapper mapper;
	public AnalysisService(AnalysisDao dao, RequestDao requestDao, AnalysisMapper mapper) {
		this.dao = dao;
		this.requestDao = requestDao;
		this.mapper = mapper;
	}
	@Transactional(readOnly = true)
	public Analysis get(Long sample, String service) {
		return mapper.map(dao.em().find(Request.class, Request.RequestPK.builder().sample(sample).service(service).build()));
	}
	@Transactional(readOnly = true)
	public Page<Analysis> list(QueryServerside query) {
		return requestDao.search(query).map(mapper::map);
	}

	@Transactional
	public void update(Long sample, String service, String info) {
		var pk = com.greencross.lims.entity.Analysis.AnalysisPK.builder().sample(sample).service(service).build();
		var entity = dao.find(pk).orElse(new com.greencross.lims.entity.Analysis(pk));
		dao.merge(entity.etc(info));
	}
}
