package ch.nliechti.repository

import org.dizitart.kno2.nitrite
import org.dizitart.no2.Nitrite
import org.slf4j.LoggerFactory
import java.io.File

object NitriteDB {

    lateinit var db: Nitrite
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("init database connection")

        this.db = nitrite(System.getenv("DB_USER") ?: "user", System.getenv("DB_PASSWORD") ?: "password") {
            file = File("deployer_db")
            autoCommitBufferSize = 2048
            compress = true
            autoCompact = false
        }
    }
}