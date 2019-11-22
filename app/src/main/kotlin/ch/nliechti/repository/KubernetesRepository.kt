package ch.nliechti.repository

import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import org.slf4j.LoggerFactory


object KubernetesRepository {

    var client: DefaultKubernetesClient
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        val kubernetesMasterURL: String = System.getenv("KUBERNETES_MASTER_URL")
                ?: ""
        val kubernetesUsername: String = System.getenv("KUBERNETES_USERNAME") ?: "minikube"
        val kubernetesClientCertData: String = System.getenv("KUBERNETES_CLIENT_CERT_DATA")?: ""
        val kubernetesClientKeyData: String = System.getenv("KUBERNETES_CLIENT_KEY_DATA") ?: "" 


        logger.info("Connect to Kubernetes cluster. Cluster: $kubernetesMasterURL, User: $kubernetesUsername")

        val config = ConfigBuilder()
                .withMasterUrl(kubernetesMasterURL)
                .withUsername(kubernetesUsername)
                .withClientCertData(kubernetesClientCertData)
                .withClientKeyData(kubernetesClientKeyData)
//                .withClientCertFile("/Users/nliechti/.minikube/client.crt")
//                .withClientKeyFile("/Users/nliechti/.minikube/client.key")
                .build()
        client = DefaultKubernetesClient(config)
    }

    fun getAllDeploymentsInNamespace(namespaceName: String): List<Deployment> {
        return client.inNamespace(namespaceName).apps().deployments().list().items
    }

    fun getAllServicesInNamespace(namespaceName: String): List<Service> {
        return client.inNamespace(namespaceName).services().list().items
    }

    fun getAllLoadBalancerInNamespace(namespaceName: String): List<Service> {
        return getAllServicesInNamespace(namespaceName)
                .filter { service -> isLoadBalancer(service) }
    }

    fun readOccupiedPorts(): List<Int> {
        val occupiedPorts = mutableListOf<Int>()
        client.inAnyNamespace().services().list().items.forEach { service ->
            if (isLoadBalancer(service)) {
                service.spec.ports.forEach { occupiedPorts.add(it.port) }
            }
        }
        return occupiedPorts
    }

    fun isLoadBalancer(config: Service) = config.spec.type == KubernetesConfigTypes.LOAD_BALANCER.value


    enum class KubernetesConfigTypes(val value: String) {
        LOAD_BALANCER("LoadBalancer")
    }
}