package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.common.models.*
import ru.mss.common.repo.DbTopicRequest
import ru.mss.common.repo.ITopicRepository
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicUpdateTest {
    abstract val repo: ITopicRepository
    protected open val updateSucc = initObjects[0]
    protected val updateConc = initObjects[1]
    private val updateIdNotFound = MssTopicId("ad-repo-update-not-found")
    protected val lockBad = MssTopicLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MssTopicLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MssTopic(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = MssUserId("owner-123"),
            status = MssTopicStatus.OPENED,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MssTopic(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MssUserId("owner-123"),
        status = MssTopicStatus.OPENED,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc = MssTopic(
        id = updateConc.id,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MssUserId("owner-123"),
        status = MssTopicStatus.OPENED,
        lock = lockBad,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTopic(DbTopicRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(reqUpdateSucc.status, result.data?.status)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTopic(DbTopicRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runTest {
        val result = repo.updateTopic(DbTopicRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitTopics("update") {
        override val initObjects: List<MssTopic> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
