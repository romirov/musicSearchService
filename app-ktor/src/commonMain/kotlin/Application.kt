package ru.mss.app.ktor

import com.auth0.jwt.JWT
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.mss.api.v1.apiV1Mapper
import ru.mss.app.common.AuthConfig
import ru.mss.app.ktor.base.resolveAlgorithm
import ru.mss.app.ktor.plugins.initAppSettings
import ru.mss.app.ktor.plugins.initPlugins
import ru.mss.app.ktor.v1.v1Topic

//запуск через main не работает
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

//запуск через gradle
//создание образа через gradle - app-ktor - ktor - runDocker
@Suppress("unused")
fun Application.module(appSettings: MssAppSettings = initAppSettings()){
    initPlugins(appSettings)

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
                    jwtCredential.payload.getClaim(AuthConfig.GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
//            authenticate("auth-jwt") {
                v1Topic(appSettings)
//            }
        }
    }
}
