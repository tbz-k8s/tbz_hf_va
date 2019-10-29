package ch.nliechti

import org.dizitart.no2.objects.Id
import java.util.*

data class GithubRepository(
        @Id var id: String,
        var name: String,
        var url: String)

data class Deployment (
        @Id var id: UUID,
        var replication: Number,
        var shouldPersist: Boolean,
        var shouldDeleteAfterShutdown: Boolean,
        var replaceCredentialPlaceholder: Boolean
) 
