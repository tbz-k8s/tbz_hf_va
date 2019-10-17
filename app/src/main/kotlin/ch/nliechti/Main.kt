package ch.nliechti

import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent
import ch.nliechti.error.addErrorHandler
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient


fun main() {

    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(readPortConfig())

//    handleDBMigration()
    val kubernetesClient = initKubernetesClient()

    app.get("/", VueComponent("<hello-world></hello-world>"))
    addErrorHandler(app)

}

fun initKubernetesClient(): KubernetesClient {
    val kubernetesClientCertData: String? = System.getenv("KUBERNETES_CLIENT_CERT_DATA")
    val kubernetesClientKeyData: String? = System.getenv("KUBERNETES_CLIENT_KEY_DATA")
    val kubernetesUsername: String? = System.getenv("KUBERNETES_USERNAME")
    val kubernetesMasterURL: String? = System.getenv("KUBERNETES_MASTER_URL")

    val config = ConfigBuilder()
            .withMasterUrl(kubernetesMasterURL ?: "https://192.168.137.100:6443")
            .withUsername(kubernetesUsername ?: "kubernetes-admin")
            .withClientCertData(kubernetesClientCertData
                    ?: "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM4akNDQWRxZ0F3SUJBZ0lJSjlEb2liUWMwMXd3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB4T1RFd01UUXhOVEV6TXpOYUZ3MHlNREV3TVRNeE5URXpNelphTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQTBTMkwxL3pBTmEwNHZFTVIKNWpBYzJiOGxMU2dTWVNzOVBxZExuSkFNcWJURm1QYXRzMnVoMXBnUjZPVlNZTkpWUnN2YnZSMmFRTEFSVUk1QwpMa25UaUpzQUhOMkdMTGZVczlyMlRRbFplaDB6MmpmT2N5TzQ0a2d3VDRiUTRzeFZ6Y3QxSjAvWFVrNXlpSXlCCjBjS1lTNEM2VHhyU0U4bkU2R2x6c1VJWUhVazNzMU9lM3FYN2RodWxTbjBET09vSW1EbXVoTVYyM0dPYlY4UVQKL3UvV3VKaTQ1T1JkVnh2NEcyKzhMaE01dnRVbVBGcFlFLzcxUWFWYmhsOUpyUnpoSGhxc2FwKzhOc1FrS3F2YwprU2JjdlFldFR1SWIvMk9DbUZpNmlXZG91ZEI5Tlo4ZlF0Q2RxUFRWN0txR0FRYlJSbTVIS3phK3hTZFUxM1ptCjBYa0VKUUlEQVFBQm95Y3dKVEFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFELzdaQllBSkFuTk9CWFl0UXVtd3FaTFZtUGZ4NnAzcG9wKwpLaVdEREVyY2RpT1hvTjZUMXB6dDNXT1Jha1U0Tjk0TE5DWVV0VzBuVzQzVCtVd3hiTFE4WEdsMVZhSlNZSGRPCnpiUkE2MEM3V3BDbnFONlZOYUNBS0tTYkdpK1BYR1dyYjZ1cVBIUEY3WHJaWForbVBFTjZxdjJrSlQxRUYyUGoKSFIxTEZwRXlqeXNmbHA3RkwxU0tUR3d2RkdVbGhmRGNpenVIdldoRWRVSFdTN0dOVGl1SGNKVDJTTlhyc1RVMgpyOVAzWU5PZTQzbHA0d1ZybVp0TkVPUzdyYkdPZnhtOVZlOG1HZVljZERIbjdCbDdYTDdWVGpQNmc2NUtYaVFNCjZIaWlSTWpMRUlDZEd6RkswS0ZFVEd6NEc0RmtsMXZ2ZHRFY2dNeFhkM05OR3k0N1RaQT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=")
            .withClientKeyData(kubernetesClientKeyData
                    ?: "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb3dJQkFBS0NBUUVBMFMyTDEvekFOYTA0dkVNUjVqQWMyYjhsTFNnU1lTczlQcWRMbkpBTXFiVEZtUGF0CnMydWgxcGdSNk9WU1lOSlZSc3ZidlIyYVFMQVJVSTVDTGtuVGlKc0FITjJHTExmVXM5cjJUUWxaZWgwejJqZk8KY3lPNDRrZ3dUNGJRNHN4VnpjdDFKMC9YVWs1eWlJeUIwY0tZUzRDNlR4clNFOG5FNkdsenNVSVlIVWszczFPZQozcVg3ZGh1bFNuMERPT29JbURtdWhNVjIzR09iVjhRVC91L1d1Smk0NU9SZFZ4djRHMis4TGhNNXZ0VW1QRnBZCkUvNzFRYVZiaGw5SnJSemhIaHFzYXArOE5zUWtLcXZja1NiY3ZRZXRUdUliLzJPQ21GaTZpV2RvdWRCOU5aOGYKUXRDZHFQVFY3S3FHQVFiUlJtNUhLemEreFNkVTEzWm0wWGtFSlFJREFRQUJBb0lCQUV0ODkyRzcraFdrN0x1cwpMTEo0bklETVpYczFZZVNsT1JYNlErUTliQVIyazNUUFdRSk9EbnFtS0poMjRkTEZOVGMyYkplRUxsMjM3SmU0CnJ4T0xXd2FLZ1hEcFZQbWdZZWVDVnE2WWVDMEVpdWp1aU5ldWhaTkwzL3RqT05FUVVzclE2UGVGN2J2clNaUVQKdU9uZEl4N2tWdGdqWXJyM2J0TTg4eDUrTHJ1cVFGMEd4RUxyVjc2M05OdVJrelJ1REFHdFBjNzFRZUJRanhsZgo2NklXdFhsZi8rcjJiMkJvaklkdUU0RE1zMTk3ZnYwWEtuMFZsVEp4eUljd2VSUE9CTmdZN3JOaWpIaU04ZWlSCkcrOGM2bjlubTBBMlRXMkkxTXduNTAvZGtXajRsUUJVcFlFKzBDNU1Pci8raGtXOGp0RFZYclJzdjFRM0kyTmoKS1g1Vm4rRUNnWUVBOVRQd3k1RVVFWHNuZ0M5VW1HTjBtWjhZaGhaMWxaYjJyVytEcGRsVG52M25OeWUxRm80bQprb2J0MWM1SDNmZ0l6WlhKV0d2ZFlDcURJTjBUdDZyak5XZ1R6OVJPWjV2NmRJSzRXbnlVVXJBZnByNXlHejllCmV2Uzg4U09CWEljZ1o4blBBRGF5MVdmU0daK3lJQzFrY3g5SktVQ0VOcS9VaUU4UUV6VTVUYmtDZ1lFQTJtT0QKUFlpbTZwTXgyTVF4VVRFSjFta3pBR2EreThTMk9iVjV6RGdCNTFsTjIzWEpDaDRuMW02YkZwWC94RXF3M3FWdwpZK3JabHFZVG1HWGt5djZRMGJFQndVREM5bzJoME84OUFrbjlwY245cCtZZFpjWFozQ0JoZTdTVWZ3T01rZm1BCitqeGduY0lJK1VmeDZVZzdvVzVhd1U3c1lNZ1JKOENhWndpL2Y4MENnWUFaQjc1UzZYSmsvbTlmMUhVTVFVckwKeThQVEhFUFFpMktPL2Mwd2h1alVXQkxjUEg5dDJBRDBBRFlURjBXYTBSRUUrMzljT1ZhVjFJRWlQNjhkbHFJdQo5M3JlUWt2bW83T0RaVHl3Yi9zc0hiSjljUDl3N0owV2JCbEZmRnhlTjZVRk0wS2dRUFVzZDdhb3l2YUI5bUJFCnFETHR0UnBLQXRUdFBUVGs0UkFDY1FLQmdRQ1RUNUxrZS93VHVTeGppcHUxcDcxNkNzWWpYQ3NSS21TODhxUHcKMUVxOGNtbnBRajlVK3RobWRYOVpzZDZ2MnE0SjBWREpwVTRXcDNvS1Q1a1FNTDBoSlNRSTVwVmNXRmN4cDE3ZwozMllnWWVzZlNVTEZOcnRwRjlLSjdscElmdTFnUGd2NVA2YndZMFZJV1haZHBLTE1sckxUT0ZpTVJBZ0daNDMvCk9GQnNkUUtCZ0RTdjRVQ091b01rMy9PdDFtcHpmUEVNOUtoNGp5OHI1bVhYMDZ6ZHg4VTFJS3ZRcXRGTnhCUVEKcmxtUGQ3cjJLaGRyY2w5RjZtY0VmUHNaaFVwNDFhNWg4TUhyNVF0aW5kWXNWdU9kakpnbk9lWUdMVC9yN1NjUQpoRW5NY1hvSnBacGRlTWt3V2xXWXo5ajA1MGlrM281Wk5KSE1GRjl5dHBma25RZnluYm96Ci0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==")
            .build()
    val client = DefaultKubernetesClient(config)
    return client
}

//fun handleDBMigration() {
//    val url = "jdbc:postgresql://127.0.0.1/test1"
//    Database.connect(url, driver = "org.postgresql.Driver", user = "postgres", password = "123")
//    Database.setInitialPowerOffCount(0)
//
//    val flyway = Flyway() 
//    flyway.setDataSource(url, "postgres", "123")
//    flyway.migrate()      
//}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}