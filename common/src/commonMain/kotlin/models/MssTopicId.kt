package ru.mss.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MssTopicId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MssTopicId("")
    }
}
