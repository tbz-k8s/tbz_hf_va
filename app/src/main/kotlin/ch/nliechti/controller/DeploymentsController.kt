package ch.nliechti.controller

import ch.nliechti.Deployment
import ch.nliechti.GithubRepository
import ch.nliechti.Repository
import ch.nliechti.repository.KubernetesRepository
import io.javalin.http.Context


object DeploymentsController {
//    fun getAllDeployments(): Deployment {
//        val server = Kubernetes 
//        val Deployment()
//    }
    
    fun getAll(ctx: Context) {
//        ctx.json(GithubRepoRepository.getAllGithubRepos())
        KubernetesRepository.client.inNamespace("default").pods()
    }
    
    fun addDeployment(ctx: Context) {
        val deployment = ctx.body<DeploymentPost>()
    }
    
    data class DeploymentPost(val deployment: Deployment, val repository: Repository)
}

