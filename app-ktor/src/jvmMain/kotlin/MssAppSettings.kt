package ru.mss.app.ktor

import ru.mss.app.common.AuthConfig
import ru.mss.app.common.IMssAppSettings
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.lib.logging.common.MssLoggerProvider

data class MssAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MssCorSettings,
    override val processor: MssTopicProcessor = MssTopicProcessor(corSettings),
    override val logger: MssLoggerProvider = MssLoggerProvider(),
    override val auth: AuthConfig = AuthConfig.TEST,
) : IMssAppSettings