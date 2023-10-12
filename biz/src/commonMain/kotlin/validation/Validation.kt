package ru.mss.biz.validation

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.chain

fun ICorChainDsl<MssContext>.validation(block: ICorChainDsl<MssContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MssState.RUNNING }
}
