package ru.mss.biz.workers

import ru.mss.common.MssContext
import ru.mss.common.models.MssError
import ru.mss.common.models.MssState
import ru.mss.common.stubs.MssStubs
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == MssStubs.BAD_ID && state == MssState.RUNNING }
    handle {
        state = MssState.FAILING
        this.errors.add(
            MssError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}