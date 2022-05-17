package com.greencross.lims.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface LocalDateTimeToEpoch {
	default LocalDateTime map(Long epoch) {
		if(epoch == null) return null;
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}
	default Long map(LocalDateTime time) {
		if(time == null) return null;
		return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
