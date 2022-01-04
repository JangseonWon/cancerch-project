package com.greencross.lims.report.builder

import com.greencross.lims.report.Resource
import com.greencross.lims.report.func.Page
import com.greencross.lims.report.func.Painter

abstract class Header<R : Resource, T : AbstractReportTemplate<R>, D : AbstractReportDto> protected constructor(
    template: T
) :
    Page<T>(template) {
    abstract fun initialize(): Painter<T, D>?
    abstract fun header(): Painter<T, D>?
    abstract fun sign(): Painter<T, D>?
}