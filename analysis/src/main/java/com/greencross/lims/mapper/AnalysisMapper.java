package com.greencross.lims.mapper;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.readonly.Request;
import com.greencross.lims.entity.readonly.Sample;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel="spring", uses={LocalDateTimeToEpoch.class, LocalDateToEpoch.class, ReportMapper.class})
public abstract class AnalysisMapper {
	@Autowired
	private RequestMapper requestMapper;
	@Mapping(target="report", source = "reports")
	public abstract com.greencross.lims.dto.Analysis map(Analysis entity);
	@Mapping(target="id", source = "sample", qualifiedByName = "serial")
	public abstract com.greencross.lims.dto.Analysis map(Request entity);
	@Named("serial")
	String serial(Sample sample) {
		return formatSampleId(sample.id());
	}
	@AfterMapping
	void sample(@MappingTarget com.greencross.lims.dto.Analysis dto, Request entity) {
		dto.request(requestMapper.map(entity));
		if(entity.analysis()!=null && !entity.analysis().isEmpty()) {
			var map = map(entity.analysis().get(0));
			if(map.batch()!=null) dto.batch(map.batch());
			if(map.row()!=null) dto.row(map.row()+0.0);
			if(map.createAt()!=null) dto.createAt(map.createAt()+0.0);
			if(map.lastModifyAt()!=null) dto.lastModifyAt(map.lastModifyAt()+0.0);
			if(map.etc()!=null) dto.etc(map.etc());
			if(map.report()!=null) dto.report(map.report());
		}
	}
	public String formatSampleId(Long id) {
		if (id == null) return null;
		else {
			String cast = String.valueOf(id);
			if (cast.length() == 15) {
				String var10000 = cast.substring(0, 8);
				return var10000 + "-" + cast.substring(8, 11) + "-" + cast.substring(11);
			} else return cast;
		}
	}
}
