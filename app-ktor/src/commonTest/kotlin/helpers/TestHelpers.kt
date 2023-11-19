package ru.mss.app.ktor.helpers

import ru.mss.app.ktor.MssAppSettings
import ru.mss.common.MssCorSettings
import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.inmemory.TopicRepoInMemory
import ru.mss.repo.stubs.TopicRepoStub

fun testSettings(repo: ITopicRepository? = null) = MssAppSettings(
    corSettings = MssCorSettings(
        repoStub = TopicRepoStub(),
        repoTest = repo ?: TopicRepoInMemory(),
        repoProd = repo ?: TopicRepoInMemory(),
    )
)
