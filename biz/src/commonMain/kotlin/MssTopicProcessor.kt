package ru.mss.biz

import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssWorkMode
import ru.mss.stubs.MssTopicStub

class MssTopicProcessor (
    @Suppress("unused")
    private val corSettings: MssCorSettings = MssCorSettings.NONE
) {
    @Suppress("RedundantSuspendModifier")
    suspend fun exec(ctx: MssContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == MssWorkMode.STUB || ctx.command in arrayOf(MssCommand.INIT, MssCommand.FINISH)) {
            "Currently working only in STUB mode."
        }

        if (ctx.state == MssState.NONE) ctx.state = MssState.RUNNING
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