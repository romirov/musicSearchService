package ru.mss.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.mss.app.ktor.MssAppSettings

fun Route.v1Topic(appSettings: MssAppSettings) {
    route("topic") {
        post("create") {
            call.createTopic(appSettings)
        }
        post("read") {
            call.readTopic(appSettings)
        }
        post("update") {
            call.updateTopic(appSettings)
        }
        post("delete") {
            call.deleteTopic(appSettings)
        }
        post("search") {
            call.searchTopic(appSettings)
        }
    }
}
