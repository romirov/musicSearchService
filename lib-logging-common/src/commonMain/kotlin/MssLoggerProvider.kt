package ru.mss.lib.logging.common

import kotlin.reflect.KClass

class MssLoggerProvider (
    private val provider: (String) -> IMssLogWrapper = { IMssLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
}