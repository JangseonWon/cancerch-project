package com.greencross.lims.mapper;

import com.greencross.lims.entity.readonly.Patient;
import com.greencross.lims.entity.readonly.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses={LocalDateToEpoch.class})
public interface PatientMapper {
	@Mapping(target="customer", ignore = true)
	com.greencross.lims.dto.Patient map(Patient entity);
	@AfterMapping
	default void customer(@MappingTarget com.greencross.lims.dto.Patient dto, Patient entity) {
		if(entity.customerName2() == null || entity.customerName2().trim().isEmpty()) dto.customer(entity.customerName());
		else dto.customer(entity.customerName2());
	}
	default String map(User entity) {
		if(entity == null) return null;
		return entity.name();
	}
}