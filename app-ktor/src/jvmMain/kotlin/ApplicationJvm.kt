package ru.mss.app.ktor

import com.auth0.jwt.JWT
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.mss.api.v1.apiV1Mapper
import ru.mss.app.common.AuthConfig.Companion.GROUPS_CLAIM
import ru.mss.app.ktor.base.*
import ru.mss.app.ktor.plugins.initAppSettings
import ru.mss.app.ktor.plugins.swagger
import ru.mss.app.ktor.v1.*
import ru.mss.lib.logging.logback.MssLogWrapperLogback

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)
private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"
@Suppress("unused")
fun Application.moduleJvm(appSettings: MssAppSettings = initAppSettings()) {

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? MssLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT.require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@moduleJvm.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }

            authenticate("auth-jwt") {
                v1Topic(appSettings)
            }
        }

        swagger(appSettings)
        static("static") {
            resources("static")
        }
    }
}
