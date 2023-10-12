package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.common.models.MssTopicId
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MssTopicId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { topicValidating.id != MssTopicId.NONE && ! topicValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = topicValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
