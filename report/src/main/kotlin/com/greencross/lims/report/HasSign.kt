package com.greencross.lims.report

import com.greencross.lims.report.builder.AbstractReportDto
import java.io.File


interface HasSign : Resource{
    fun stylePerson(): TextStyle
    fun person(name: String): Person?
    fun labels(): Array<SignLabel>

    class Person(
        name:String,
        license:String,
        sign:File
    )

    interface SignLabel{
        fun label(): String
        fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?>
    }
}