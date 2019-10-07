package ch.nliechti

import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent
import ch.nliechti.error.addErrorHandler

fun main() {

    val app = Javalin.create { config ->
        config.enableWebjars()
    }.start(readPortConfig())
    
    app.get("/", VueComponent("<hello-world></hello-world>"))
    addErrorHandler(app)
}

private fun readPortConfig(): Int {
    val configuredPort = System.getenv("WEBSERVER_PORT")
    return if (configuredPort != null) {
        Integer.parseInt(configuredPort)
    } else 7000
}