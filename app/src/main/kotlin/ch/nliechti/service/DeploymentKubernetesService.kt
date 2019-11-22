package ch.nliechti.service

import ch.nliechti.Repository
import ch.nliechti.Trainee
import ch.nliechti.controller.DeploymentController
import ch.nliechti.controller.DeploymentsController
import ch.nliechti.kubernetesModels.TBZ_DEPLOYMENT_LABEL
import ch.nliechti.kubernetesModels.TBZ_TRAINEE_MAIL
import ch.nliechti.kubernetesModels.TBZ_TRAINEE_NAME
import ch.nliechti.repository.KubernetesRepository
import ch.nliechti.repository.SchoolClassRepository
import ch.nliechti.util.DeploymentUtil
import io.fabric8.kubernetes.api.model.EnvVar
import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.client.KubernetesClientException

object DeploymentKubernetesService {
    fun createKubernetesConfig(repo: Repository, deploymentPost: DeploymentController.DeploymentPost) {
        val originalDataSource = repo.dataSource
        val loadedConfigs = KubernetesRepository.client.load(originalDataSource.byteInputStream()).get()

        val preparedConfig = replacePlaceholder(loadedConfigs)

        if (deploymentPost.deployment.replication > 0) {
            val totalReplications = deploymentPost.deployment.replication
            repeat(totalReplications) {
                createDeploymentInNamespace(deploymentPost, it, preparedConfig, null)
            }
        } else {
            val schoolClass = SchoolClassRepository.getSchoolClass(deploymentPost.schoolClassName!!)
            schoolClass?.trainees?.forEachIndexed { index, trainee ->
                createDeploymentInNamespace(deploymentPost, index, preparedConfig, trainee)
            }
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
            config.metadata.annotations
                    .filter { annotation -> annotation.key.matches(Regex("^tbz-replace-env.*")) }
                    .forEach { annotation ->
                        if (config is io.fabric8.kubernetes.api.model.apps.Deployment) {
                            config.spec.template.spec.containers.forEach { container ->
                                container.env.forEach { env -> if (env.name == annotation.value) envs.add(env) }
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


    private fun createDeploymentInNamespace(deploymentPost: DeploymentController.DeploymentPost, prefix: Int, loadedConfigs: List<HasMetadata>, trainee: Trainee?) {
        try {
            val namespace = "${deploymentPost.deployment.name}-$prefix"
            replacePersistentVolumeClaimSubpaths(loadedConfigs, namespace)
            val doneableNamespace = KubernetesRepository.client.namespaces()
                    .createNew()
                    .withNewMetadata()
                    .withLabels(mapOf(TBZ_DEPLOYMENT_LABEL to deploymentPost.deployment.name))
                    .withName(namespace)

            trainee?.let { it -> doneableNamespace.withAnnotations(createTraineeAnnotations(it)) }
            doneableNamespace.endMetadata().done()

            setLoadBalancerConfig(loadedConfigs, prefix)
            KubernetesRepository.client.resourceList(loadedConfigs).inNamespace(namespace).createOrReplace()
        } catch (e: KubernetesClientException) {
            DeploymentsController.deleteDeploymentByName(deploymentPost.deployment.name)
            throw e
        }
    }

    private fun replacePersistentVolumeClaimSubpaths(loadedConfigs: List<HasMetadata>, namespaceName: String) {
        loadedConfigs.filterIsInstance<Deployment>()
                .forEach { config ->
                    config.spec.template.spec.containers.forEach { container ->
                        container.volumeMounts.forEach { volumeMount ->
                            volumeMount.subPath = namespaceName + "_" + volumeMount.subPath
                        }
                    }
                }
    }

    private fun createTraineeAnnotations(trainee: Trainee): MutableMap<String, String> {
        return mutableMapOf(
                Pair(TBZ_TRAINEE_NAME, trainee.name),
                Pair(TBZ_TRAINEE_MAIL, trainee.email))
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