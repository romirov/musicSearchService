package ru.mss.common

import ru.mss.common.repo.ITopicRepository
import ru.mss.lib.logging.common.MssLoggerProvider

data class MssCorSettings(
    val loggerProvider: MssLoggerProvider = MssLoggerProvider(),
    val repoStub: ITopicRepository = ITopicRepository.NONE,
    val repoTest: ITopicRepository = ITopicRepository.NONE,
    val repoProd: ITopicRepository = ITopicRepository.NONE,
) {
    companion object {
        val NONE = MssCorSettings()
    }
}