package ru.mss.biz.groups

import ru.mss.common.MssContext
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.chain

fun ICorChainDsl<MssContext>.operation(title: String, command: MssCommand, block: ICorChainDsl<MssContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == MssState.RUNNING }
}