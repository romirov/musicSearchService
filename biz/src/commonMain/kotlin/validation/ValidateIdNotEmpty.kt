package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.errorValidation
import ru.mss.common.helpers.fail
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { topicValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
