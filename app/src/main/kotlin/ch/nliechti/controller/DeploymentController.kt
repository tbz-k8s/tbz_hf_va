package ch.nliechti.controller

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

object DeploymentController {

    fun getDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")
        val namespaces: List<Namespace> = KubernetesRepository.client.namespaces().withLabel(TBZ_DEPLOYMENT_LABEL, deploymentName).list().items
        namespaces.forEach { namespace ->
            namespace.metadata.labels[TBZ_REPLACE_ENV]?.let {
                //                KubernetesRepository.client.inNamespace(namespace.)
            }
        }
    }

//    data class DeploymentResponse(val externalAccess: MutableList<ExternalAccess>, )
//    data class ExternalAccess(val ip: String, val port: Number)

    fun addDeployment(ctx: Context) {
        val deploymentPost = ctx.body<DeploymentsController.DeploymentPost>()
        deploymentPost.deployment.name = deploymentPost.deployment.name.toLowerCase()
        val repo = GithubRepoRepository.getGithubRepo(deploymentPost.repositoryId)
        repo?.let {
            createKubernetesConfig(repo, deploymentPost)
        } ?: ctx.res.sendError(400, "No repository with id ${deploymentPost.repositoryId} found")
    }

    private fun createKubernetesConfig(repo: Repository, deploymentPost: DeploymentsController.DeploymentPost) {
        val totalReplications = deploymentPost.deployment.replication
        val originalDataSource = repo.dataSource

        val loadedConfigs = KubernetesRepository.client.load(originalDataSource.byteInputStream()).get()

        repeat(totalReplications) {
            val preparedConfig = replacePlaceholder(loadedConfigs)
            createDeploymentInNamespace(deploymentPost, it, preparedConfig)
        }
    }

    private fun replacePlaceholder(loadedConfigs: List<HasMetadata>): List<HasMetadata> {
        loadedConfigs.forEach { config ->
            config.metadata.labels[TBZ_REPLACE_ENV]?.let { label ->
                replaceEnv(config, label)
            }
        }
        return loadedConfigs
    }

    private fun replaceEnv(config: HasMetadata, label: String) {
        if (config is io.fabric8.kubernetes.api.model.apps.Deployment) {
            config.spec.template.spec.containers.forEach { container ->
                container.env.forEach { env ->
                    if (env.name == label) {
                        env.value = DeploymentUtil.getRandomValueForEnv()
                    }
                }
            }
        }
    }

    private fun createDeploymentInNamespace(deploymentPost: DeploymentsController.DeploymentPost, prefix: Int, loadedConfigs: List<HasMetadata>) {
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

}