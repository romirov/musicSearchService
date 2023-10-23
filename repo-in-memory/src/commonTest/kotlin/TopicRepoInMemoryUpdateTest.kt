package ru.mss.repo.inmemory

import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.RepoTopicUpdateTest

class TopicRepoInMemoryUpdateTest : RepoTopicUpdateTest() {
    override val repo: ITopicRepository = TopicRepoInMemory(
        initObjects = initObjects,
    )
}
