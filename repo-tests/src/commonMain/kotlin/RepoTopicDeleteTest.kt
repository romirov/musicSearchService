package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.repo.DbTopicIdRequest
import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.BaseInitTopics
import ru.otus.otuskotlin.marketplace.backend.repo.tests.runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicDeleteTest {
    abstract val repo: ITopicRepository

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteTopic(DbTopicIdRequest(successId, lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readTopic(DbTopicIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runTest {
        val result = repo.deleteTopic(DbTopicIdRequest(concurrencyId, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitTopics("delete") {
        override val initObjects: List<MssTopic> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val successId = MssTopicId(initObjects[0].id.asString())
        val notFoundId = MssTopicId("topic-repo-delete-notFound")
        val concurrencyId = MssTopicId(initObjects[1].id.asString())
    }
}
