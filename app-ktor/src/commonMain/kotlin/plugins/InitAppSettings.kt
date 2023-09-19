package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.biz.MssTopicProcessor

fun Application.initAppSettings(): MssAppSettings {
    return MssAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = MssTopicProcessor(),
    )
}