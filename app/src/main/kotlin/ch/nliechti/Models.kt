package ch.nliechti

import okhttp3.OkHttpClient
import okhttp3.Request
import org.dizitart.no2.objects.Id

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
        get() {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .build()
            return client.newCall(request).execute().body()?.string() ?: ""
        }
}

data class Deployment(
        @Id var name: String,
        var replication: Int,
        var shouldPersist: Boolean,
        var shouldDeleteAfterShutdown: Boolean
)

data class SchoolClass(
        @Id val name: String,
        val trainees: List<Trainee>
)

data class Trainee(
        val name: String,
        val email: String
)
