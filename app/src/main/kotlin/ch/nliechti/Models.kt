package ch.nliechti

import org.dizitart.no2.objects.Id
import java.net.URL
import java.util.*

interface Repository {
    val id: String
    val name: String
    val dataSource: String
}

data class GithubRepository(
        @Id override var id: String,
        override var name: String,
        var url: String) : Repository {
    override val dataSource: String
        get() = URL(url).readText()
}

data class Deployment(
        @Id var id: String,
        var replication: Number,
        var shouldPersist: Boolean,
        var shouldDeleteAfterShutdown: Boolean
)
