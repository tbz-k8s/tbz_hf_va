package ch.nliechti

import ch.nliechti.controller.GithubRepoController
import ch.nliechti.error.addErrorHandler
import com.charleskorn.kaml.Yaml
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
apiVersion: apps/v1beta2 # for versions before 1.8.0 use apps/v1beta1
kind: Deployment
metadata:
  name: osticket
spec:
  replicas: 1
  selector:
    matchLabels:
      app: osticket
  template:
    metadata:
      labels:
        app: osticket
        group: customer
        tier: frontend
    spec:
      containers:
      - name: osticket
        image: campbellsoftwaresolutions/osticket
        env:
        - name: MYSQL_HOST
          value: osticket-mysql
        - name: MYSQL_PASSWORD
          value: secret        
        ports:
        - containerPort: 80
          name: osticket
          """.trimIndent()
    val result = Yaml.default.parse(ReplicaSet.serializer(), input)

//    GithubRepoRepository.addGithubRepo(GithubRepository(name = "Bla test", url = URL("http://test.com"), id = UUID.randomUUID()) )
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