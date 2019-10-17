package ch.nliechti.util

import java.util.*

object UUIDUtil {
    fun fromString(uuid: String): UUID? {
        val convertedUuid: UUID? = try {
            UUID.fromString(uuid)
        } catch (e: IllegalArgumentException) {
            return null
        }
        return convertedUuid
    }
}