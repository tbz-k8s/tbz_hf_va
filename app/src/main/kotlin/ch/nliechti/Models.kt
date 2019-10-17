package ch.nliechti

import java.net.URL

data class Deployment(
        val id: String,
        val name: String,
        val replication: Int
)

data class Repository(
        val id: String,
        val name: String,
        val url: URL
)
