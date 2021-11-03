package com.greencross.lims.service

import com.greencross.lims.data.Worklist
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface WorklistToDto {
    fun toDto(entity: com.greencross.lims.entity.Worklist): Worklist
}