package com.greencross.lims.mapper;

import com.greencross.lims.entity.readonly.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={SampleMapper.class, ServiceMapper.class, LocalDateTimeToEpoch.class, LocalDateToEpoch.class})
public interface RequestMapper {
	com.greencross.lims.dto.Request map(Request entity);
}