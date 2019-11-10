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
                ?: "https://192.168.64.6:8443"
        val kubernetesUsername: String = System.getenv("KUBERNETES_USERNAME") ?: "minikube"
        val kubernetesClientCertData: String = System.getenv("KUBERNETES_CLIENT_CERT_DATA")
                ?: "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM5RENDQWR5Z0F3SUJBZ0lJWTZkMnA5R3ZCYWN3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB4T1RBNE1UUXhNelEwTURCYUZ3MHlNREV4TURFeE5ESTBNREZhTURZeApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sc3dHUVlEVlFRREV4SmtiMk5yWlhJdFptOXlMV1JsCmMydDBiM0F3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRRHNIcy85S05kZmhIbjUKeTIwcU9lc05oM0lyYk5mN2t1VUlidnNXbWxxaENuTVdUMDd6bjdEL3ZvRVgvODQwYTl2d3hMNDFac29SclA0YQo2bWVEandCTCt0L1lMeVQyK0lVVkhhSFNWL2tWT3Y4U3JZY1F3OTh2VGtnaHJ1eE04YW9aZEp0RmVhd0g2cE1WClZjZjlRQzhRQTdrNjJodmQ0UzErZEZsK1RQZUdnNnVPUTc3YWZnNm1CL1I4WHRXcTltYTl5VWNUT2luL01IbTAKcGFpY0tzTkdmLzR6TkNLKzdkdmlDWW50MVJmdjZJWSs0OEYxUkFnSmNkeTBoSUFLUS9ITkdIVjVIbm4wbTJxNQpEazhFbWhaM3N4TFRQUHpJMi9oUHhoTzlSV0o1UGZZT1ptSldyTE1SZUI2WFVZdTF6Nmh5TkdRUFVSa1B6dmx1Ck8yRTNqcGxIQWdNQkFBR2pKekFsTUE0R0ExVWREd0VCL3dRRUF3SUZvREFUQmdOVkhTVUVEREFLQmdnckJnRUYKQlFjREFqQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFiVUExMktDamcvdHBCdml4NTJpRW11ckM2VVRzaklsdAplaU90ODg1dFZPNjQzVFdLdGc5bXF1ejhOb3BkdldDMGRhbjR3R3dBMlpXMGFOM2dtY0JheHdweUxqZjNuSlBLCmV6Rmt1aEFoZHFEMy9tZE1qWFlYUHZVdlMzejdXNjN0dTluVFhIUnRTMHNuOXFCQU00WndWVm85aGtYRWg3U0IKU2EwRFhWbVlLTWNGVysxU3Z4U3ZuQ0s3eDBzMnJnTTBCd25rTnFEekU3bzhXUEdSRlhWTkZicVBpNllXYlpyaApXMEtmMnA4Yk9vNXllT0FYWHZoeUxSWTJWM29teHRrVGRCUkhiRUU5YWRsQ2VGbHc3empaQXhVbU54Z2JqQ2d1CmhrQ0swNG52NFFvQTlrWlY0Q3NUZnYwTm9NY0R0bC9yZ2xyUVZlU2NJWnpMRjk0NEZoSDVQdz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K"
        val kubernetesClientKeyData: String = System.getenv("KUBERNETES_CLIENT_KEY_DATA")
                ?: "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFcFFJQkFBS0NBUUVBN0I3UC9TalhYNFI1K2N0dEtqbnJEWWR5SzJ6WCs1TGxDRzc3RnBwYW9RcHpGazlPCjg1K3cvNzZCRi8vT05HdmI4TVMrTldiS0VheitHdXBuZzQ4QVMvcmYyQzhrOXZpRkZSMmgwbGY1RlRyL0VxMkgKRU1QZkwwNUlJYTdzVFBHcUdYU2JSWG1zQitxVEZWWEgvVUF2RUFPNU90b2IzZUV0Zm5SWmZrejNob09yamtPKwoybjRPcGdmMGZGN1ZxdlptdmNsSEV6b3AvekI1dEtXb25DckRSbi8rTXpRaXZ1M2I0Z21KN2RVWDcraUdQdVBCCmRVUUlDWEhjdElTQUNrUHh6UmgxZVI1NTlKdHF1UTVQQkpvV2Q3TVMweno4eU52NFQ4WVR2VVZpZVQzMkRtWmkKVnF5ekVYZ2VsMUdMdGMrb2NqUmtEMUVaRDg3NWJqdGhONDZaUndJREFRQUJBb0lCQVFDRlJ0OGZobTRLL0R2ZApRM1hhMzlaTlRLYWszRXNOTlYwUjVaTFVyMHQ0bXUxYkttam1HcjFObmJwQVVhT1M4MDZ5R0Z0RXlVQkZ1ZCswClVac2NKeHVEeDVlUjd3cHVYTmpCa25oRXdtTldHSGJobEJFbzVXYVV6UnJwNkZKNUcrOW5idW8xelBmTmdCcUYKY0dYeURNaUR3WlpNSWRDWTdRRkEzMnVrWHMxWW51UUhGY3FnYjUzM3lUSTVBSjNtNzFoUXN5YjdhSmQrTXBLRwpPelpjbDc1dFdDUTBtUWRHN2pzWHZ3UzRWdXlUbVdXaElNcCsrKzdqR0h2RjYvcFF4bmdmaTRKRm80cGV5T1IvClhaMFZUVVFHUC9pL3c2eGlyNTdoSVJGbngyR21RZGNWK2lSZ3o1ams0RVJBc21Bc1RlUWw2bEdjZDdtNkVwYjEKcnB2VHdzUUJBb0dCQU81UmpDQ1EycHUvWmpYZzA0ZXZWQnZkSVhOdFUvU21yZ0FBQmIwcW9OSnQ5NzFtckhxQQpZczY1UTYvdnVCcEhCSVVWNzIxZUNxTHVUUlozTHRFaERNNmpzeGRGVDZTMlMxc1JvdkNtRUdncUI3V0dqR09HCjdKUUplZjlsOElWSHUzZEkwY2o4aTBYR0dmUXg3VmN5dXp6SEIvWXJNN0JFYitRbU1KalBWRWYxQW9HQkFQMmoKZzZ6NG9Gd0tQSGljOXNXOHBraXBwK0ppZUR5YlVhUFUzSjhaVFlTOXpUbWYwTVB0SHZmYnYzUXZGYVljNUEwbwpCaStOYjQ0VTVGYmNFQ1V3aEpXY2hzdDdUQkNUcVJBS0xFZHR2eGpvcUxiRXZnWHlkZ1pETW52cmlKNXRmOXNzCnFKR2kyYmc1c3V0R2Mrd2JIdSs5TzR2Mk5aMlpQTnlJT1p3Wm55TExBb0dBUHZiV29lZ0RpZkM0clhWNERibzUKNjU2eDd0QXN4VStnV3ltbVdvTS91RGtMb2tQVlFBeFpqcXFPQXBiQU9sa1hEWjV0QXFVOUJDMFh1dDdFUk1hSgpuckxMWjFPTnBrUHZodUVTL3FleFlBUHl3M2dIOFdlOWE2TlJVelA0cHczN3JwbFRxOWxsWm9yZllwQXFWOFowCmg2dENKRHpuekNOWXVKdUtiV1VockYwQ2dZRUEvRk9sZFhqS240WVhicTVJMmFLNWttUWxmcFFUcGNlbXltUEQKTUF3T0VGWjZTaTl4a3RqZERCdHpOZlI1aHc0SzJnUE5VNVZUTzNnUlNWQVlSOGNpdjJ3K3RVUHVBamZENHc4Ygp5ZHVtVnZOdWxKVFpyalR0c1lFOFFnZXBuR0syNWFaaDlMQ2NCQ2h5bEprOFRkRVZUODlqNC9PR2wwaVJRWnZNCkpEMWhFVTBDZ1lFQWluVUp4OGFveXpLU0tPTnl1alJqeDdNZTl6OHpXV1ZaZFU3cDRqY084RzN0a2ZTaXM2MDcKMkpkdU1kMnFtOXp5WlgycjZvTFl5N3pCNi9KQm4xRmdVRmIxRHVBWmgzNDJ4TUUrN2puSCtDclZIa2dQU1hRaApxZUk4MWxreDZjcDNEaGs3WUk1WnJBZzFXczhXMVJEM1dZTU5JSWdjRklHMzd3TnFpblNEQmxRPQotLS0tLUVORCBSU0EgUFJJVkFURSBLRVktLS0tLQo="


        logger.info("Connect to Kubernetes cluster. Cluster: $kubernetesMasterURL, User: $kubernetesUsername")

        val config = ConfigBuilder()
                .withMasterUrl(kubernetesMasterURL)
                .withUsername(kubernetesUsername)
//                .withClientCertData(kubernetesClientCertData)
//                .withClientKeyData(kubernetesClientKeyData)
                .withClientCertFile("/Users/nliechti/.minikube/client.crt")
                .withClientKeyFile("/Users/nliechti/.minikube/client.key")
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