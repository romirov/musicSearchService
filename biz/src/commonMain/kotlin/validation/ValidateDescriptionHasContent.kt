package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker


// TODO-validation-7: пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<MssContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { topicValidating.description.isNotEmpty() && ! topicValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "description",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
