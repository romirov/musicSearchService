package ru.mss.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.mss.api.v1.apiV1Mapper
import ru.mss.app.ktor.plugins.initAppSettings
import ru.mss.app.ktor.plugins.initPlugins
import ru.mss.app.ktor.v2.v1Topic

//запуск через main не работает
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

//запуск через gradle
//создание образа через gradle - app-ktor - ktor - runDocker
@Suppress("unused")
fun Application.module(appSettings: MssAppSettings = initAppSettings()){
    initPlugins(appSettings)

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
    }
}
