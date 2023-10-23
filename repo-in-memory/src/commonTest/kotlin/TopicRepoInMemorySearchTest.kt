package ru.mss.repo.inmemory

import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.RepoTopicSearchTest

class TopicRepoInMemorySearchTest : RepoTopicSearchTest() {
    override val repo: ITopicRepository = TopicRepoInMemory(
        initObjects = initObjects
    )
}
