package ru.mss.biz.workers

import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.common.models.MssError
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == MssState.RUNNING }
    handle {
        fail(
            MssError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}