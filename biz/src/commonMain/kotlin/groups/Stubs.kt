package ru.mss.biz.groups

import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.common.models.MssWorkMode
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.chain

fun ICorChainDsl<MssContext>.stubs(title: String, block: ICorChainDsl<MssContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MssWorkMode.STUB && state == MssState.RUNNING }
}