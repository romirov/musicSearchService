package ru.mss.common.helpers

import ru.mss.common.MssContext
import ru.mss.common.models.MssError

fun Throwable.asMssError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MssError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun MssContext.addError(vararg error: MssError) = errors.addAll(error)