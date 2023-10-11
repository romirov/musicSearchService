package ru.mss.biz.workers

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == MssState.NONE }
    handle { state = MssState.RUNNING }
}