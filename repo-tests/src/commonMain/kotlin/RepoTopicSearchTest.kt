package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssUserId
import ru.mss.common.repo.DbTopicFilterRequest
import ru.mss.common.repo.ITopicRepository
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicSearchTest {
    abstract val repo: ITopicRepository

    protected open val initializedObjects: List<MssTopic> = initObjects

    @Test
    fun search() = runRepoTest {
        val result = repo.searchTopic(DbTopicFilterRequest(searchString = "ad2"))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitTopics("search") {

        val searchOwnerId = MssUserId("owner-124")
        override val initObjects: List<MssTopic> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", status = MssTopicStatus.OPENED),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", status = MssTopicStatus.OPENED),
        )
    }
}
