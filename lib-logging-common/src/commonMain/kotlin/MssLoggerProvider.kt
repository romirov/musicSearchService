package ru.mss.lib.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class MssLoggerProvider (
    private val provider: (String) -> IMssLogWrapper = { IMssLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}