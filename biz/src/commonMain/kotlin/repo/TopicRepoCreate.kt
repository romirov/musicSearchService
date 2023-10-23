package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.repo.DbTopicRequest
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление топика в БД"
    on { state == MssState.RUNNING }
    handle {
        val request = DbTopicRequest(topicRepoPrepare)
        val result = topicRepo.createTopic(request)
        val resultTopic = result.data
        if (result.isSuccess && resultTopic != null) {
            topicRepoDone = resultTopic
        } else {
            state = MssState.FAILING
            errors.addAll(result.errors)
        }
    }
}
