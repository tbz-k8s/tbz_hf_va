package ch.nliechti.controller

import ch.nliechti.GithubRepository
import ch.nliechti.repository.GithubRepoRepository
import ch.nliechti.util.UUIDUtil.fromString
import io.javalin.http.Context
import java.util.*

object GithubRepoController {
    fun getAll(ctx: Context) {
        ctx.json(GithubRepoRepository.getAllGithubRepos())
    }

    fun getOne(ctx: Context) {
        fromString(ctx.pathParam("repo-id"))?.let { uuid ->
            val repo: GithubRepository? = GithubRepoRepository.getGithubRepo(uuid)
            repo?.let { ctx.json(it) } ?: ctx.status(404)
        } ?: ctx.status(400)

    }

    fun createRepo(ctx: Context) {
        val repository = ctx.body<GithubRepository>()
        repository.id = UUID.randomUUID().toString()
        GithubRepoRepository.addGithubRepo(listOf(repository))
        ctx.status(204)
    }

    fun deleteRepo(ctx: Context) {
        fromString(ctx.pathParam("repo-id"))?.let { uuid ->
            GithubRepoRepository.deleteGithubRepoById(listOf(uuid))
        } ?: ctx.status(400)
    }
}