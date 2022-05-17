package com.greencross.lims.mapper;

import com.greencross.lims.entity.readonly.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
	com.greencross.lims.dto.Service map(Service entity);
}