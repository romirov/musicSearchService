package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker


fun ICorChainDsl<MssContext>.finishTopicValidation(title: String) = worker {
    this.title = title
    on { state == MssState.RUNNING }
    handle {
        topicValidated = topicValidating
    }
}

fun ICorChainDsl<MssContext>.finishTopicFilterValidation(title: String) = worker {
    this.title = title
    on { state == MssState.RUNNING }
    handle {
        topicFilterValidated = topicFilterValidating
    }
}
