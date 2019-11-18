package ch.nliechti.controller

import ch.nliechti.Trainee
import ch.nliechti.kubernetesModels.TBZ_DEPLOYMENT_LABEL
import ch.nliechti.kubernetesModels.TBZ_TRAINEE_MAIL
import ch.nliechti.kubernetesModels.TBZ_TRAINEE_NAME
import ch.nliechti.repository.GithubRepoRepository
import ch.nliechti.repository.KubernetesRepository
import ch.nliechti.repository.KubernetesRepository.getAllDeploymentsInNamespace
import ch.nliechti.service.DeploymentKubernetesService
import ch.nliechti.service.MailService
import io.fabric8.kubernetes.api.model.EnvVar
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.javalin.http.Context

object DeploymentController {

    fun getDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")
        ctx.json(getDeployment(deploymentName))
    }

    private fun getDeployment(deploymentName: String): DeploymentsResponse {
        val namespaces: List<Namespace> = KubernetesRepository.client.namespaces().withLabel(TBZ_DEPLOYMENT_LABEL, deploymentName).list().items
        val deployments = mutableListOf<DeploymentResponse>()
        var totalReady = 0
        var totalDeployments = 0
        namespaces.forEach { namespace ->
            val deploymentsInNamespace = getAllDeploymentsInNamespace(namespace.metadata.name)
            val state = getDeploymentState(deploymentsInNamespace)
            totalReady += state.ready
            totalDeployments += state.total

            deployments.add(DeploymentResponse(
                    getExternalAccess(namespace),
                    getClusterAccess(namespace),
                    getAllReplacesEnv(deploymentsInNamespace),
                    getTraineeForNamespace(namespace),
                    state,
                    namespace.metadata.name.replace("$deploymentName-", "")))
        }

        return DeploymentsResponse(deployments, totalReady, totalDeployments)
    }

    private fun getTraineeForNamespace(namespace: Namespace): Trainee? {
        val annotations = namespace.metadata.annotations
        val trainee = Trainee("", "")
        annotations[TBZ_TRAINEE_NAME]?.let { trainee.name = it } ?: return null
        annotations[TBZ_TRAINEE_MAIL]?.let { trainee.email = it } ?: return null
        return trainee
    }

    private fun getClusterAccess(namespace: Namespace): List<ClusterAccess> {
        val services = KubernetesRepository.getAllServicesInNamespace(namespace.metadata.name)
        return services
                .filter { service -> service.spec.clusterIP != "None" }
                .map { service ->
                    ClusterAccess(service.spec.clusterIP, service.spec.ports.map { port -> port.nodePort })
                }
    }

    private fun getDeploymentState(deployments: List<Deployment>): DeploymentState {
        var ready = 0
        deployments.forEach {
            if (it.status.availableReplicas == 1) ready++
        }
        return DeploymentState(ready, deployments.size)
    }

    private fun getExternalAccess(namespace: Namespace): List<ExternalAccess> {
        val loadBalancer = KubernetesRepository.getAllLoadBalancerInNamespace(namespace.metadata.name)
        return loadBalancer.map { lb ->
            ExternalAccess(lb.status.loadBalancer.ingress.getOrNull(0)?.ip
                    ?: "", lb.spec.ports.map { port -> port.port })
        }
    }

    private fun getAllReplacesEnv(deployments: List<Deployment>): List<EnvVar> {
        return DeploymentKubernetesService.getAllReplacableEnvs(deployments)
    }


    data class DeploymentResponse(val externalAccess: List<ExternalAccess>,
                                  val clusterAccess: List<ClusterAccess>,
                                  val replacedEnvs: List<EnvVar>,
                                  val trainee: Trainee?,
                                  val state: DeploymentState,
                                  val deploymentNumber: String)

    data class DeploymentsResponse(val deployments: List<DeploymentResponse>, val totalReady: Number, val totalDeployments: Number)
    data class ExternalAccess(val ip: String, val ports: List<Int>)
    data class ClusterAccess(val ip: String, val ports: List<Int>)
    data class DeploymentState(val ready: Int, val total: Int)

    fun addDeployment(ctx: Context) {
        val deploymentPost = ctx.body<DeploymentPost>()
        deploymentPost.deployment.name = deploymentPost.deployment.name.toLowerCase()
        val repo = GithubRepoRepository.getGithubRepo(deploymentPost.repositoryId)

        if (deploymentPost.schoolClassName.isNotBlank()) deploymentPost.deployment.replication = 0
        repo?.let {
            DeploymentKubernetesService.createKubernetesConfig(repo, deploymentPost)
        } ?: ctx.res.sendError(400, "No repository with id ${deploymentPost.repositoryId} found")
    }


    data class DeploymentPost(val deployment: ch.nliechti.Deployment, val repositoryId: String, val schoolClassName: String)

    fun sendMailForDeployment(deploymentName: String, deploymentNumber: String?) {
        val deployment = getDeployment(deploymentName)
        if (deploymentNumber != null) {
            val deploymentNo = Integer.valueOf(deploymentNumber)
            sendMailForDeploymentPart(deployment.deployments[deploymentNo])
        } else {
            deployment.deployments.forEach { deployment ->
                sendMailForDeploymentPart(deployment)
            }
        }
    }

    fun sendMailForAllDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")
        sendMailForDeployment(deploymentName, null)
    }

    fun sendMailForOneDeployment(ctx: Context) {
        val deploymentNumber: String = ctx.pathParam("deployment-number")
        val deploymentName: String = ctx.pathParam("deployment-name")
        sendMailForDeployment(deploymentName, deploymentNumber)
    }

    private fun sendMailForDeploymentPart(deploymentResponse: DeploymentResponse) {
        var mailBody = "Login für ${deploymentResponse.trainee?.name} \n"
        mailBody += "\n\nENV Variablen: \n"
        deploymentResponse.replacedEnvs.forEach { mailBody += "${it.name}: ${it.value} \n" }

        mailBody += "\n\n External Access: "
        deploymentResponse.externalAccess.forEach { mailBody += "${it.ip}:${it.ports[0]} \n" }

        mailBody += "\n\n Cluster Access: "
        deploymentResponse.clusterAccess.forEach { mailBody += "${it.ip}:${it.ports[0]} \n" }

//        MailService.sendMail(deploymentResponse.trainee?.email ?: "", mailBody)
        println("Real email: ${deploymentResponse.trainee?.email}")
        MailService.sendMail("nliechti@nliechti.ch", mailBody)
    }
}