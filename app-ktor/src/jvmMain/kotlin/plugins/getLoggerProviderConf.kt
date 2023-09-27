package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.lib.logging.common.MssLoggerProvider

actual fun Application.getLoggerProviderConf(): MssLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
//        "kmp" -> MssLoggerProvider { mssLoggerKermit(it) }
        "logback", null -> MssLoggerProvider { mssLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
    }