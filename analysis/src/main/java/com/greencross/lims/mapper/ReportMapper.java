package com.greencross.lims.mapper;

import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.readonly.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", uses={LocalDateTimeToEpoch.class, LocalDateToEpoch.class})
public interface ReportMapper {
	@Mapping(target="creator", source = "createBy")
	@Mapping(target="publisher", source = "publishBy")
	@Mapping(target="fileName", source = "name")
	@Mapping(target="fileSize", source = "size")
	com.greencross.lims.dto.Report map(Report entity);

	default com.greencross.lims.dto.Report map(List<Report> reports){
		if(reports==null || reports.isEmpty()) return null;
		return map(reports.stream().max(Comparator.comparing(r->r.pk().createAt())).get());
	}
	default String map(User entity) {
		if(entity == null) return null;
		return entity.name();
	}
}
