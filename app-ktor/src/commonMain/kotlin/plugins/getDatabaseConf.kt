package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.common.repo.ITopicRepository

expect fun Application.getDatabaseConf(type: TopicDbType): ITopicRepository

enum class TopicDbType(val confName: String) {
    PROD("prod"), TEST("test")
}
