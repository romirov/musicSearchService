package ru.mss.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.biz.addTestPrincipal
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.*
import ru.mss.common.repo.DbTopicResponse
import ru.mss.repo.tests.TopicRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = MssUserId("321")
    private val command = MssCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = TopicRepositoryMock(
        invokeCreateTopic = {
            DbTopicResponse(
                isSuccess = true,
                data = MssTopic(
                    id = MssTopicId(uuid),
                    title = it.topic.title,
                    description = it.topic.description,
                    ownerId = userId,
                    status = MssTopicStatus.OPENED
                )
            )
        }
    )
    private val settings = MssCorSettings(
        repoTest = repo
    )
    private val processor = MssTopicProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicRequest = MssTopic(
                title = "abc",
                description = "abc",
                status = MssTopicStatus.OPENED,
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(MssState.FINISHING, ctx.state)
        assertNotEquals(MssTopicId.NONE, ctx.topicResponse.id)
        assertEquals("abc", ctx.topicResponse.title)
        assertEquals("abc", ctx.topicResponse.description)
        assertEquals(MssTopicStatus.OPENED, ctx.topicResponse.status)
    }
}
