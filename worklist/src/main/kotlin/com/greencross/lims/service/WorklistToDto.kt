package com.greencross.lims.service

import com.greencross.lims.data.Worklist
import org.mapstruct.Mapper
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueMappingStrategy= NullValueMappingStrategy.RETURN_DEFAULT)
interface WorklistToDto {
    fun toDto(entity: com.greencross.lims.entity.Worklist): Worklist
}