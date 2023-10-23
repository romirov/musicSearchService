package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.repo.DbTopicIdRequest
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение топика из БД"
    on { state == MssState.RUNNING }
    handle {
        val request = DbTopicIdRequest(topicValidated)
        val result = topicRepo.readTopic(request)
        val resultTopic = result.data
        if (result.isSuccess && resultTopic != null) {
            topicRepoRead = resultTopic
        } else {
            state = MssState.FAILING
            errors.addAll(result.errors)
        }
    }
}
