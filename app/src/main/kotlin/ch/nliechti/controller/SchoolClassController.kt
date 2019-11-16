package ch.nliechti.controller

import ch.nliechti.SchoolClass
import ch.nliechti.repository.SchoolClassRepository
import ch.nliechti.service.CSVService
import io.javalin.http.Context

object SchoolClassController {
    fun getAll(ctx: Context) {
        ctx.json(SchoolClassRepository.getAllClasses())
    }

    fun getOne(ctx: Context) {
        val schoolClass: SchoolClass? = SchoolClassRepository.getSchoolClass(ctx.pathParam("class-name"))
        schoolClass?.let { ctx.json(it) } ?: ctx.status(404)
    }

    fun createSchoolClass(ctx: Context) {
        val uploadedFile = ctx.uploadedFile("file")
        uploadedFile?.content?.let {
            val schoolClass = CSVService.readTrainees(it)
            SchoolClassRepository.addSchoolClass(listOf(schoolClass))
        } ?: ctx.status(500)

        ctx.status(204)
    }

    fun deleteSchoolClass(ctx: Context) {
        SchoolClassRepository.deleteSchoolClass(ctx.pathParam("class-name"))
    }
}