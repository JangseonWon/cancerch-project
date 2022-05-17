package com.greencross.lims.mapper;

import com.greencross.lims.entity.readonly.Sample;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={PatientMapper.class})
public interface SampleMapper {
	com.greencross.lims.dto.Sample map(Sample entity);
}