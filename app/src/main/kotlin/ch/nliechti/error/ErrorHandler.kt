package ch.nliechti.error

import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent


fun addErrorHandler(app: Javalin) {
    app.error(404, "html", VueComponent("<not-found></not-found>"))
    app.error(500, "html", VueComponent("<internal-server-error></internal-server-error>"))
}