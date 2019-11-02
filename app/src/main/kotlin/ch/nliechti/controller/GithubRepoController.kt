package ch.nliechti.controller

import ch.nliechti.GithubRepository
import ch.nliechti.Repository
import ch.nliechti.repository.GithubRepoRepository
import io.javalin.http.Context
import java.util.*

object GithubRepoController {
    fun getAll(ctx: Context) {
        ctx.json(GithubRepoRepository.getAllGithubRepos())
    }

    fun getOne(ctx: Context) {
        val repo: Repository? = GithubRepoRepository.getGithubRepo(ctx.pathParam("repo-id"))
        repo?.let { ctx.json(it) } ?: ctx.status(404)

    }

    fun createRepo(ctx: Context) {
        val repository = ctx.body<GithubRepository>()
        repository.id = UUID.randomUUID().toString()
        GithubRepoRepository.addGithubRepo(listOf(repository))
        ctx.status(204)
    }

    fun deleteRepo(ctx: Context) {
        GithubRepoRepository.deleteGithubRepoById(listOf(ctx.pathParam("repo-id")))
    }
}