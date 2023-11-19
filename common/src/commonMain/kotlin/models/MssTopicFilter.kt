package ru.mss.common.models

data class MssTopicFilter(
    var searchString: String = "",
    var searchPermissions: MutableSet<MssSearchPermissions> = mutableSetOf(),
)
