package ru.mss.repo.inmemory

import ru.mss.repo.tests.RepoTopicCreateTest

class TopicRepoInMemoryCreateTest : RepoTopicCreateTest() {
    override val repo = TopicRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}