package ru.mss.common.helpers

import ru.mss.common.MssContext
import ru.mss.common.models.MssCommand

fun MssContext.isUpdatableCommand() =
    this.command in listOf(MssCommand.CREATE, MssCommand.UPDATE, MssCommand.DELETE)