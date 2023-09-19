package ru.mss.app.ktor

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.mss.api.v1.apiV1Mapper
import ru.mss.app.ktor.plugins.initAppSettings
import ru.mss.app.ktor.v2.v1Topic
import ru.mss.biz.MssTopicProcessor

//запуск через gradle
//создание образа через gradle - app-ktor - ktor - runDocker
@Suppress("unused")
fun Application.module(appSettings: MssAppSettings = initAppSettings()){
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }

            v1Topic(appSettings)
        }

        static("static") {
            resources("static")
        }
    }
}

//запуск через main не работает
fun main(){
    embeddedServer(CIO, port = 8080) {
        module()
    }.start(wait = true)
}
