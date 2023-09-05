package ru.mss.biz

import ru.mss.common.MssContext
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssWorkMode
import ru.mss.stubs.MssTopicStub

class MssTopicProcessor {
    suspend fun exec(ctx: MssContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == MssWorkMode.STUB) {
            "Currently working only in STUB mode."
        }

        when (ctx.command) {
            MssCommand.SEARCH -> {
                ctx.topicsResponse.addAll(MssTopicStub.prepareSearchList("Неизвестная композиция", MssTopicStatus.OPENED))
            }
            else -> {
                ctx.topicResponse = MssTopicStub.get()
            }
        }
    }
}