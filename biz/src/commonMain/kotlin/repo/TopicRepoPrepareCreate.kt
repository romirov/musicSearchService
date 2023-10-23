package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MssState.RUNNING }
    handle {
        topicRepoRead = topicValidated.deepCopy()
        topicRepoPrepare = topicRepoRead

    }
}
