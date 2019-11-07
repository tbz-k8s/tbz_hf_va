package ch.nliechti.controller

import ch.nliechti.Deployment
import ch.nliechti.Repository
import ch.nliechti.kubernetesModels.TBZ_DEPLOYMENT_LABEL
import ch.nliechti.repository.GithubRepoRepository
import ch.nliechti.repository.KubernetesRepository
import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.Service
import io.javalin.http.Context


object DeploymentsController {

    fun getAll(ctx: Context) {
        val tbzDeployedNamespaces: List<Namespace> = KubernetesRepository.client.namespaces().withLabel(TBZ_DEPLOYMENT_LABEL).list().items

        val deployments = mutableMapOf<String, Int>()
        tbzDeployedNamespaces.forEach { deployment ->
            val deployedLabel: String = deployment.metadata.labels[TBZ_DEPLOYMENT_LABEL]!!
            deployments.compute(deployedLabel) { _, count -> count?.let { count + 1 } ?: 1 }
        }

        ctx.json(deployments.map { DeploymentsResponse(it.key, it.value) })
    }

    data class DeploymentsResponse(val name: String, val replications: Int)

    fun getDeployment(ctx: Context) {
        val repo: String = ctx.pathParam("deployment-name")

//        KubernetesRepository.client.namespaces().withName()
    }

    fun addDeployment(ctx: Context) {
        val deploymentPost = ctx.body<DeploymentPost>()
        deploymentPost.deployment.name = deploymentPost.deployment.name.toLowerCase()
        val repo = GithubRepoRepository.getGithubRepo(deploymentPost.repositoryId)
        repo?.let {
            createKubernetesConfig(repo, deploymentPost)
        } ?: ctx.res.sendError(400, "No repository with id ${deploymentPost.repositoryId} found")
    }

    fun deleteDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")

        val namespaces = KubernetesRepository.client.namespaces().list().items
        val namespacesToDelete = namespaces.filter { it.metadata.name.matches(Regex("^$deploymentName-\\d+")) }
        KubernetesRepository.client.resourceList(namespacesToDelete).delete()
    }

    private fun createKubernetesConfig(repo: Repository, deploymentPost: DeploymentPost) {
        val totalReplications = deploymentPost.deployment.replication
        val originalDataSource = repo.dataSource

        val loadedConfigs = KubernetesRepository.client.load(originalDataSource.byteInputStream()).get()

        repeat(totalReplications) {
            createDeploymentInNamespace(deploymentPost, it, loadedConfigs)
        }
    }

    private fun createDeploymentInNamespace(deploymentPost: DeploymentPost, prefix: Int, loadedConfigs: MutableList<HasMetadata>) {
        val namespace = "${deploymentPost.deployment.name}-$prefix"
        KubernetesRepository.client.namespaces()
                .createNew()
                .withNewMetadata()
                .withLabels(mapOf(TBZ_DEPLOYMENT_LABEL to deploymentPost.deployment.name))
                .withName(namespace)
                .endMetadata()
                .done()
        setLoadBalancerConfig(loadedConfigs, prefix)
        KubernetesRepository.client.resourceList(loadedConfigs).inNamespace(namespace).createOrReplace()
    }

    private fun setLoadBalancerConfig(configs: List<HasMetadata>, configCounter: Int) {
        val occupiedPorts: List<Int> = KubernetesRepository.readOccupiedPorts()
        configs.forEach { config ->
            if (config is Service && KubernetesRepository.isLoadBalancer(config)) {
                config.spec.ports.forEach { publicPort ->
                    var port = 11000 + configCounter
                    occupiedPorts.forEach { ocPort -> if (ocPort == port) port++ }
                    publicPort.port = port
                }
            }
        }
    }

    data class DeploymentPost(val deployment: Deployment, val repositoryId: String)

}

