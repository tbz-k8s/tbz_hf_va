package ch.nliechti

import ch.nliechti.controller.GithubRepoController
import ch.nliechti.error.addErrorHandler
import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent


fun main() {

    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(readPortConfig())

    app.get("/", VueComponent("<hello-world></hello-world>"))
    addGithubRepoController(app)
    addErrorHandler(app)

//    GithubRepoRepository.addGithubRepo(GithubRepository(name = "Bla test", url = URL("http://test.com"), id = UUID.randomUUID()) )
}

fun addGithubRepoController(app: Javalin) {
    app.get("/api/v1/repos", GithubRepoController::getAll)
    app.get("/api/v1/repos/:repo-id", GithubRepoController::getOne)
    app.post("api/v1/repo/", GithubRepoController::getOne)
}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}