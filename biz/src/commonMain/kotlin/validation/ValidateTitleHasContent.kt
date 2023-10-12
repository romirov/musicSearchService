package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { topicValidating.title.isNotEmpty() && ! topicValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "title",
            violationCode = "noContent",
            description = "field must contain leters"
        )
        )
    }
}
