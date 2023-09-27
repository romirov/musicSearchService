package ru.mss.common

import ru.mss.lib.logging.common.MssLoggerProvider

data class MssCorSettings(
    val loggerProvider: MssLoggerProvider = MssLoggerProvider(),
) {
    companion object {
        val NONE = MssCorSettings()
    }
}