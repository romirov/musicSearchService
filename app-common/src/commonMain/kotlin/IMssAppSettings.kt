package ru.mss.app.common

import ru.mss.biz.MssTopicProcessor

interface IMssAppSettings {
    val processor: MssTopicProcessor
}