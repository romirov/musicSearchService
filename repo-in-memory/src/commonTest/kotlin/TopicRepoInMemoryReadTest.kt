package ru.mss.repo.inmemory

import ru.mss.common.repo.ITopicRepository
import ru.otus.otuskotlin.marketplace.backend.repo.tests.RepoTopicReadTest

class TopicRepoInMemoryReadTest: RepoTopicReadTest() {
    override val repo: ITopicRepository = TopicRepoInMemory(
        initObjects = initObjects
    )
}
