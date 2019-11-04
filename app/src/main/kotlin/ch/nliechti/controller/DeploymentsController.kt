package ch.nliechti.controller

import ch.nliechti.Deployment
import ch.nliechti.Repository
import ch.nliechti.controller.DeploymentsController.KubernetesConfigTypes.LOAD_BALANCER
import ch.nliechti.repository.GithubRepoRepository
import ch.nliechti.repository.KubernetesRepository
import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Service
import io.javalin.http.Context


object DeploymentsController {
    fun getAll(ctx: Context) {
//        ctx.json(GithubRepoRepository.getAllGithubRepos())
        KubernetesRepository.client.inNamespace("default").pods()
    }

    fun addDeployment(ctx: Context) {
        val deploymentPost = ctx.body<DeploymentPost>()
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
                .withName(namespace)
                .endMetadata()
                .done()
        setLoadBalancerConfig(loadedConfigs, prefix)
        KubernetesRepository.client.resourceList(loadedConfigs).inNamespace(namespace).createOrReplace()
    }

    private fun setLoadBalancerConfig(configs: List<HasMetadata>, configCounter: Int) {
        configs.forEach { config ->
            if (config is Service && config.spec.type == LOAD_BALANCER.value) {
                config.spec.ports.forEach {
                    it.port = 11000 + configCounter
                }
            }
        }
    }

    data class DeploymentPost(val deployment: Deployment, val repositoryId: String)

    enum class KubernetesConfigTypes(val value: String) {
        LOAD_BALANCER("LoadBalancer")
    }
}

