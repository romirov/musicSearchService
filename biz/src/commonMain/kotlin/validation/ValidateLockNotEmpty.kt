package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.errorValidation
import ru.mss.common.helpers.fail
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { topicValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
