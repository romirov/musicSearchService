package ru.mss.app.ktor

import ru.mss.app.common.IMssAppSettings
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings

data class MssAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MssCorSettings,
    override val processor: MssTopicProcessor = MssTopicProcessor(corSettings),
): IMssAppSettings
