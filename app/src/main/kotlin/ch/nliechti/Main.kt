package ch.nliechti

import ch.nliechti.controller.GithubRepoController
import ch.nliechti.error.addErrorHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.fabric8.kubernetes.api.model.apps.ReplicaSet
import io.fabric8.kubernetes.client.utils.Serialization
import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent


fun main() {

    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(readPortConfig())

    app.get("/", VueComponent("<hello-world></hello-world>"))
    addGithubRepoController(app)
    addErrorHandler(app)

    val input = """

          """.trimIndent()

    val mapper = ObjectMapper(YAMLFactory())
    val replicaSet = mapper.readValue<ReplicaSet>(input)
    print(replicaSet)
}

fun addGithubRepoController(app: Javalin) {
    app.get("/settings/repos", VueComponent("<github-repos></github-repos>"))
    app.get("/settings/repo/:repo-id", VueComponent("<github-repo></github-repo>"))
    app.get("/settings/repo", VueComponent("<github-repo></github-repo>"))
    app.get("/api/v1/repos", GithubRepoController::getAll)
    app.get("/api/v1/repos/:repo-id", GithubRepoController::getOne)
    app.post("/api/v1/repo", GithubRepoController::createRepo)
    app.delete("/api/v1/repos/:repo-id", GithubRepoController::deleteRepo)
}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}