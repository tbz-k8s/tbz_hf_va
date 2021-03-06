package ch.nliechti.error

import ch.nliechti.service.MailService
import io.fabric8.kubernetes.client.KubernetesClientException
import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.VueComponent


fun addErrorHandler(app: Javalin) {
    app.error(404, "html", VueComponent("<not-found></not-found>"))
    app.error(500, "html", VueComponent("<internal-server-error></internal-server-error>"))

    app.exception(KubernetesClientException::class.java) { e, ctx ->
        ctx.res.sendError(500, e.status?.message ?: e.message)
    }

    app.exception(org.dizitart.no2.exceptions.UniqueConstraintException::class.java) { e, ctx ->
        ctx.res.sendError(500, e.message)
    }

    app.exception(MailService.MailSenderException::class.java) { e, ctx ->
        ctx.res.sendError(500, e.message)
    }
}
