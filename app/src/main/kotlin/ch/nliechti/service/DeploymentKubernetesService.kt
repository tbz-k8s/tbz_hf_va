package ch.nliechti.service

import ch.nliechti.Repository
import ch.nliechti.controller.DeploymentController
import ch.nliechti.kubernetesModels.TBZ_DEPLOYMENT_LABEL
import ch.nliechti.repository.KubernetesRepository
import ch.nliechti.util.DeploymentUtil
import io.fabric8.kubernetes.api.model.EnvVar
import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment

object DeploymentKubernetesService {
    fun createKubernetesConfig(repo: Repository, deploymentPost: DeploymentController.DeploymentPost) {
        val totalReplications = deploymentPost.deployment.replication
        val originalDataSource = repo.dataSource

        val loadedConfigs = KubernetesRepository.client.load(originalDataSource.byteInputStream()).get()

        repeat(totalReplications) {
            val preparedConfig = replacePlaceholder(loadedConfigs)
            createDeploymentInNamespace(deploymentPost, it, preparedConfig)
        }
    }

    private fun replacePlaceholder(loadedConfigs: List<HasMetadata>): List<HasMetadata> {
        val envs = getAllReplacableEnvs(loadedConfigs)
        replaceEnv(envs)
        return loadedConfigs
    }

    fun getAllReplacableEnvs(configs: List<HasMetadata>): List<EnvVar> {
        val envs = mutableListOf<EnvVar>()
        configs.forEach { config ->
            config.metadata.labels
                    .filter { label -> label.key.matches(Regex("^tbz-replace-env.*")) }
                    .forEach { label ->
                        if (config is io.fabric8.kubernetes.api.model.apps.Deployment) {
                            config.spec.template.spec.containers.forEach { container ->
                                container.env.forEach { env -> if (env.name == label.value) envs.add(env) }
                            }
                        }
                    }
        }
        return envs
    }

    private fun replaceEnv(envs: List<EnvVar>) {
        envs.forEach { env ->
            env.value = DeploymentUtil.getRandomValueForEnv()
        }
    }


    private fun createDeploymentInNamespace(deploymentPost: DeploymentController.DeploymentPost, prefix: Int, loadedConfigs: List<HasMetadata>) {
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
            // Cannot extract into method because the compiler cannot handle typeOf check for the config in function.
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