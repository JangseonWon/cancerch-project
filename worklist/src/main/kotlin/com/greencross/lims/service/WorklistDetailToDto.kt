package com.greencross.lims.service

import com.greencross.lims.data.Request_
import com.greencross.lims.data.Sample
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface WorklistDetailToDto {

}