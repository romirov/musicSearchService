package ru.mss.app.common

import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.lib.logging.common.MssLoggerProvider

interface IMssAppSettings {
    val processor: MssTopicProcessor
    val corSettings: MssCorSettings
    val logger: MssLoggerProvider
    val auth: AuthConfig
}