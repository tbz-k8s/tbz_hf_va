package ch.nliechti.controller

import ch.nliechti.GithubRepository
import ch.nliechti.repository.GithubRepoRepository
import io.javalin.http.Context
import java.util.*

object GithubRepoController {
    fun getAll(ctx: Context) {
        ctx.json(GithubRepoRepository.getAllGithubRepos())
    }

    fun getOne(ctx: Context) {
        val uuid: UUID = try { UUID.fromString(ctx.pathParam("repo-id")) } catch (e: IllegalArgumentException) { ctx.status(400); return}
        val repo: GithubRepository? = GithubRepoRepository.getGithubRepo(uuid)
        repo?.let { ctx.json(it) } ?: ctx.status(404)
    }
}