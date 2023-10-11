package ru.mss.biz.workers

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.stubs.MssStubs
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker
import ru.mss.stubs.MssTopicStub

fun ICorChainDsl<MssContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MssStubs.SUCCESS && state == MssState.RUNNING }
    handle {
        state = MssState.FINISHING
        topicsResponse.addAll(MssTopicStub.prepareSearchList(topicFilterRequest.searchString, MssTopicStatus.OPENED))
    }
}