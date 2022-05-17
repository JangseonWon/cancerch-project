package com.greencross.lims.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface LocalDateToEpoch {
	default LocalDate map(Long epoch) {
		if(epoch == null) return null;
		return LocalDate.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}
	default long map(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
