package ru.mss.mappers.v1.exceptions

import ru.mss.common.models.MssCommand

class UnknownMssCommand(command: MssCommand) : Throwable("Wrong command $command at mapping toTransport stage")
