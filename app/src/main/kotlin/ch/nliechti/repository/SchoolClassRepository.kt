package ch.nliechti.repository

import ch.nliechti.SchoolClass
import org.dizitart.kno2.filters.eq
import org.dizitart.kno2.getRepository

object SchoolClassRepository {
    fun getAllClasses(): List<SchoolClass> {
        var schoolClasses: List<SchoolClass> = emptyList()
        NitriteDB.db.getRepository<SchoolClass> {
            schoolClasses = find().toList()
        }
        return schoolClasses
    }

    fun getSchoolClass(name: String): SchoolClass? {
        var schoolClass: SchoolClass? = null
        NitriteDB.db.getRepository<SchoolClass> {
            schoolClass = find(SchoolClass::name eq name).firstOrDefault()
        }
        return schoolClass
    }

    fun addSchoolClass(classes: List<SchoolClass>) {
        NitriteDB.db.getRepository<SchoolClass> {
            classes.forEach { insert(it) }
        }
    }

    fun deleteSchoolClass(className: String) {
        NitriteDB.db.getRepository<SchoolClass> {
            remove(SchoolClass::name eq className)
        }
    }
}