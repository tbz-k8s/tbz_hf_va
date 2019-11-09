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

    fun getDeployment(ctx: Context) {
        val deploymentName: String = ctx.pathParam("deployment-name")
        val namespaces: List<Namespace> = KubernetesRepository.client.namespaces().withLabel(TBZ_DEPLOYMENT_LABEL, deploymentName).list().items
        namespaces.forEach { namespace ->
            namespace.metadata.labels[TBZ_REPLACE_ENV]?.let {
                //                KubernetesRepository.client.inNamespace(namespace.)
            }
        }
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

    private fun createDeploymentInNamespace(deploymentPost: DeploymentPost, prefix: Int, loadedConfigs: List<HasMetadata>) {
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

