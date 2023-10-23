package ru.mss.biz.general

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.models.MssWorkMode
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MssWorkMode.STUB }
    handle {
        topicResponse = topicRepoDone
        topicsResponse = topicsRepoDone
        state = when (val st = state) {
            MssState.RUNNING -> MssState.FINISHING
            else -> st
        }
    }
}
