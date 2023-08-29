package ru.mss.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MssRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MssRequestId("")
    }
}
