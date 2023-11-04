package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.repo.stubs.TopicRepoStub

fun Application.initAppSettings(): MssAppSettings {
    val corSettings = MssCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(TopicDbType.TEST),
        repoProd = getDatabaseConf(TopicDbType.PROD),
        repoStub = TopicRepoStub(),
    )

    return MssAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MssTopicProcessor(),
    )
}