package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssUserId
import ru.mss.common.repo.DbTopicRequest
import ru.mss.common.repo.ITopicRepository
import ru.otus.otuskotlin.marketplace.backend.repo.tests.runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicUpdateTest {
    abstract val repo: ITopicRepository
    protected open val updateSucc = initObjects[0]
    private val updateIdNotFound = MssTopicId("topic-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        MssTopic(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = MssUserId("owner-123"),
            status = MssTopicStatus.OPENED
        )
    }
    private val reqUpdateNotFound = MssTopic(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MssUserId("owner-123"),
        status = MssTopicStatus.OPENED
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

    companion object : BaseInitTopics("update") {
        override val initObjects: List<MssTopic> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
