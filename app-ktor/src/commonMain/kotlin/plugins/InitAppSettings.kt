package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings

fun Application.initAppSettings(): MssAppSettings {
    val corSettings = MssCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = TopicRepoInMemory(),
        repoProd = TopicRepoInMemory(),
        repoStub = TopicRepoStub(),
    )

    return MssAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MssTopicProcessor(),
    )
}