package ru.mss.app.common

import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings

interface IMssAppSettings {
    val processor: MssTopicProcessor
    val corSettings: MssCorSettings
}