package ch.nliechti

import ch.nliechti.controller.DeploymentsController
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
    addDeploymentsRoutes(app)
    addErrorHandler(app)
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

fun addDeploymentsRoutes(app: Javalin) {
    app.get("/deployments", VueComponent("<deployments></deployments>"))
    app.get("/deployment", VueComponent("<deployment></deployment>"))
    app.get("/api/v1/deployments", DeploymentsController::getAll)

    app.get("/api/v1/deployment/:deployment-name", DeploymentsController::getDeployment)
    app.post("/api/v1/deployment", DeploymentsController::addDeployment)
    app.delete("/api/v1/deployment/:deployment-name", DeploymentsController::deleteDeployment)
}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}