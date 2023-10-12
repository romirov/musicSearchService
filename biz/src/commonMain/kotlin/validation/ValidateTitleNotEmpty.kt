package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

// TODO-validation-4: смотрим пример COR DSL валидации
fun ICorChainDsl<MssContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { topicValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "title",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
