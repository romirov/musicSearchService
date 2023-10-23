package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MssState.RUNNING }
    handle {
        topicRepoPrepare = topicRepoRead.deepCopy().apply {
            this.title = topicValidated.title
            description = topicValidated.description
            status = topicValidated.status
        }
    }
}
