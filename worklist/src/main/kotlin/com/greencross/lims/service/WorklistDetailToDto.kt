package com.greencross.lims.service

import com.greencross.lims.data.Work_
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface WorklistDetailToDto {
    fun toDto(entity: com.greencross.lims.entity.Work): Work_
}