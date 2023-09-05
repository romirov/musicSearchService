package ru.mss.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.mss.api.v1.apiV1Mapper
import ru.mss.app.ktor.v2.v1Topic
import ru.mss.biz.MssTopicProcessor

fun Application.module(processor: MssTopicProcessor = MssTopicProcessor()) {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }

            v1Topic(processor)
        }
    }
}

fun main() {
    embeddedServer(CIO, port = 8080) {
        module()
    }.start(wait = true)
}
