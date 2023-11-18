package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.repo.DbTopicIdRequest
import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.BaseInitTopics
import ru.mss.repo.tests.runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicReadTest {
    abstract val repo: ITopicRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTopic(DbTopicIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readTopic(DbTopicIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTopics("delete") {
        override val initObjects: List<MssTopic> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MssTopicId("topic-repo-read-notFound")

    }
}
