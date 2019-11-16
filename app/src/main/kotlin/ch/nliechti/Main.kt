package ch.nliechti

import ch.nliechti.controller.DeploymentController
import ch.nliechti.controller.DeploymentsController
import ch.nliechti.controller.GithubRepoController
import ch.nliechti.controller.SchoolClassController
import ch.nliechti.error.addErrorHandler
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.plugin.rendering.vue.JavalinVue
import io.javalin.plugin.rendering.vue.VueComponent


fun main() {

    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(readPortConfig())
    if (System.getenv("DOCKER_DEPLOY") == "true") {
        JavalinVue.rootDirectory("/vue", Location.CLASSPATH)
    }

    app.get("/", VueComponent("<hello-world></hello-world>"))
    addGithubRepoController(app)
    addDeploymentsRoutes(app)
    addSchoolClassRoutes(app)
    addErrorHandler(app)
}

fun addSchoolClassRoutes(app: Javalin) {
    app.get("/settings/school-classes", VueComponent("<school-classes></school-classes>"))
    app.get("/settings/school-class/:class-name", VueComponent("<school-class></school-class>"))
    app.get("/settings/school-class", VueComponent("<school-class></school-class>"))
    
    app.get("/api/v1/school-classes", SchoolClassController::getAll)
    app.get("/api/v1/school-class/:class-name", SchoolClassController::getOne)
    app.post("/api/v1/school-class", SchoolClassController::createSchoolClass)
    app.delete("/api/v1/school-class/:class-name", SchoolClassController::deleteSchoolClass)
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
    app.get("/deployment/:deployment-name", VueComponent("<deployment-detail></deployment-detail>"))
    app.get("/api/v1/deployments", DeploymentsController::getAll)

    app.get("/api/v1/deployment/:deployment-name", DeploymentController::getDeployment)
    app.post("/api/v1/deployment", DeploymentController::addDeployment)
    app.delete("/api/v1/deployment/:deployment-name", DeploymentsController::deleteDeployment)

}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}