package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.lib.logging.common.MssLoggerProvider

expect fun Application.getLoggerProviderConf(): MssLoggerProvider