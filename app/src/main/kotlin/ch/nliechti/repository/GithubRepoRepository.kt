package ch.nliechti.repository

import ch.nliechti.GithubRepository
import ch.nliechti.Repository
import ch.nliechti.repository.NitriteDB.db
import org.dizitart.kno2.filters.eq
import org.dizitart.kno2.getRepository
import java.util.*

object GithubRepoRepository {

    fun getAllGithubRepos(): List<GithubRepository> {
        var repos: List<GithubRepository> = emptyList()
        val repository = db.getRepository<GithubRepository> {
            repos = find().toList()
        }
        return repos
    }

    fun getGithubRepo(id: String): Repository? {
        var repo: GithubRepository? = null
        db.getRepository<GithubRepository> {
            repo = find(GithubRepository::id eq id).firstOrDefault()
        }
        return repo
    }

    fun addGithubRepo(repos: List<GithubRepository>) {
        db.getRepository<GithubRepository> {
            repos.forEach { insert(it) }
        }
    }

    fun deleteGithubRepo(repos: List<GithubRepository>) {
        db.getRepository<GithubRepository> {
            repos.forEach { repo: GithubRepository? ->
                remove(repo)
            }
        }
    }

    fun deleteGithubRepoById(repos: List<String>) {
        db.getRepository<GithubRepository> {
            repos.forEach {
                remove(GithubRepository::id eq it)
            }
        }
    }
}