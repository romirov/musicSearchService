package ru.mss.biz.repo

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.repo.DbTopicFilterRequest
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == MssState.RUNNING }
    handle {
        val request = DbTopicFilterRequest(
            searchString = topicFilterValidated.searchString
        )
        val result = topicRepo.searchTopic(request)
        val resultTopics = result.data
        if (result.isSuccess && resultTopics != null) {
            topicsRepoDone = resultTopics.toMutableList()
        } else {
            state = MssState.FAILING
            errors.addAll(result.errors)
        }
    }
}
