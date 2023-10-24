package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.errorValidation
import ru.mss.common.helpers.fail
import ru.mss.common.models.MssTopicLock
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MssTopicId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { topicValidating.lock != MssTopicLock.NONE && !topicValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = topicValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
