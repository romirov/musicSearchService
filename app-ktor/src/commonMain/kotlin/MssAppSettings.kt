package ru.mss.app.ktor

import ru.mss.app.common.IMssAppSettings
import ru.mss.biz.MssTopicProcessor

data class MssAppSettings(
    val appUrls: List<String> = emptyList(),
    override val processor: MssTopicProcessor = MssTopicProcessor(),
): IMssAppSettings
