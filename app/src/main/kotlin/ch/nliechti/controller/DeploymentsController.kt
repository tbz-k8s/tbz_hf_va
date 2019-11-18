package ch.nliechti.controller

import ch.nliechti.Deployment
import ch.nliechti.Repository
import ch.nliechti.kubernetesModels.TBZ_DEPLOYMENT_LABEL
import ch.nliechti.kubernetesModels.TBZ_REPLACE_ENV
import ch.nliechti.repository.GithubRepoRepository
import ch.nliechti.repository.KubernetesRepository
import ch.nliechti.util.DeploymentUtil
import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.Service
import io.javalin.http.Context


object DeploymentsController {

    fun getAll(ctx: Context) {
        val tbzDeployedNamespaces: List<Namespace> = KubernetesRepository.client.namespaces().withLabel(TBZ_DEPLOYMENT_LABEL).list().items

        val deployments = mutableMapOf<String, DeploymentsResponse>()
        tbzDeployedNamespaces.forEach { deployment ->
            extractDeploymentInfos(deployment, deployments)
        }

        ctx.json(deployments.values)
    }

    private fun extractDeploymentInfos(deployment: Namespace, deployments: MutableMap<String, DeploymentsResponse>) {
        deployment.status
        val deployedLabel: String = deployment.metadata.labels[TBZ_DEPLOYMENT_LABEL]!!
        deployments.compute(deployedLabel) { _, response ->
            val newResponse: DeploymentsResponse = response?.let {
                it.replications = it.replications + 1
                it
            } ?: DeploymentsResponse(deployedLabel, 1, deployment.status.phase)
            newResponse
        }
    }

    data class DeploymentsResponse(val name: String, var replications: Int, val status: String)

    fun deleteDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")
        deleteDeploymentByName(deploymentName)
    }
    
    fun deleteDeploymentByName(deploymentName: String) {
        val namespaces = KubernetesRepository.client.namespaces().list().items
        val namespacesToDelete = namespaces.filter { it.metadata.name.matches(Regex("^$deploymentName-\\d+")) }
        KubernetesRepository.client.resourceList(namespacesToDelete).delete()
    }
}

