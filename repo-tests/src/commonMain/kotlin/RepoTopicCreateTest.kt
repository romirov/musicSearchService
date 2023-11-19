package ru.mss.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.common.models.*
import ru.mss.common.repo.DbTopicRequest
import ru.mss.common.repo.ITopicRepository
import ru.otus.otuskotlin.marketplace.backend.repo.tests.runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoTopicCreateTest {
    abstract val repo: ITopicRepository

    protected open val lockNew: MssTopicLock = MssTopicLock("20000000-0000-0000-0000-000000000002")

    private val createObj = MssTopic(
        title = "create object",
        description = "create object description",
        ownerId = MssUserId("owner-123"),
        status = MssTopicStatus.OPENED,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTopic(DbTopicRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: MssTopicId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.status, result.data?.status)
        assertNotEquals(MssTopicId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitTopics("create") {
        override val initObjects: List<MssTopic> = emptyList()
    }
}
