package ru.mss.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.mss.biz.MssTopicProcessor

fun Route.v1Topic(processor: MssTopicProcessor) {
    route("topic") {
        post("create") {
            call.createTopic(processor)
        }
        post("read") {
            call.readTopic(processor)
        }
        post("update") {
            call.updateTopic(processor)
        }
        post("delete") {
            call.deleteTopic(processor)
        }
        post("search") {
            call.searchTopic(processor)
        }
    }
}
