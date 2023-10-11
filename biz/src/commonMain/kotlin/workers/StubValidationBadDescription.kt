package ru.mss.biz.workers

import ru.mss.common.MssContext
import ru.mss.common.models.MssError
import ru.mss.common.models.MssState
import ru.mss.common.stubs.MssStubs
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == MssStubs.BAD_DESCRIPTION && state == MssState.RUNNING }
    handle {
        state = MssState.FAILING
        this.errors.add(
            MssError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}