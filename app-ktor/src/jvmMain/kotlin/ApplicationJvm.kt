package ru.mss.app.ktor

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.mss.app.ktor.plugins.initAppSettings
import ru.mss.app.ktor.plugins.swagger
import ru.mss.lib.logging.logback.MssLogWrapperLogback

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"
@Suppress("unused")
// Referenced in application.conf_
fun Application.moduleJvm(appSettings: MssAppSettings = initAppSettings()) {
    module(appSettings)

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? MssLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }

    routing {
        swagger(appSettings)
        static("static") {
            resources("static")
        }
    }
}