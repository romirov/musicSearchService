package ru.mss.repo.inmemory

import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.RepoTopicDeleteTest

class TopicRepoInMemoryDeleteTest : RepoTopicDeleteTest() {
    override val repo: ITopicRepository = TopicRepoInMemory(
        initObjects = initObjects
    )
}
