package ch.nliechti

import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.api.model.apps.ReplicaSet
import org.dizitart.no2.objects.Id
import java.net.URL
import java.util.*
import kotlinx.serialization.*

//data class Deployment(
//        @Id val id: UUID,
//        val name: String,
//        val replication: Int
//)

data class GithubRepository(
        @Id val id: UUID,
        val name: String,
        val url: URL)

@Serializable
class Deployment: Deployment () {
    
}

@Serializable
data class ReplicaSet(val bla: String): ReplicaSet() 