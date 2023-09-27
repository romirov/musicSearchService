package ru.mss.common.models

data class MssError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: Level = Level.ERROR,
){
    @Suppress("unused")
    enum class Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
