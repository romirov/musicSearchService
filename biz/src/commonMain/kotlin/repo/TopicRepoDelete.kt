package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.repo.DbTopicIdRequest
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление топика из БД по ID"
    on { state == MssState.RUNNING }
    handle {
        val request = DbTopicIdRequest(topicRepoPrepare)
        val result = topicRepo.deleteTopic(request)
        if (!result.isSuccess) {
            state = MssState.FAILING
            errors.addAll(result.errors)
        }
        topicRepoDone = topicRepoRead
    }
}
