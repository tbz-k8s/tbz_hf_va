package ch.nliechti

import org.dizitart.no2.objects.Id
import java.net.URL
import java.util.*

data class GithubRepository(
        @Id val id: UUID,
        val name: String,
        val url: URL)

data class Deployment (
        @Id val id: UUID,
        val replication: Number,
        val shouldPersist: Boolean,
        val shouldDeleteAfterShutdown: Boolean,
        val replaceCredentialPlaceholder: Boolean
) 
