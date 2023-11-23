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

class BizRepoReadTest {

    private val userId = MssUserId("321")
    private val command = MssCommand.READ
    private val initTopic = MssTopic(
        id = MssTopicId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        status = MssTopicStatus.OPENED,
    )
    private val repo by lazy { TopicRepositoryMock(
        invokeReadTopic = {
            DbTopicResponse(
                isSuccess = true,
                data = initTopic,
            )
        }
    ) }
    private val settings by lazy {
        MssCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { MssTopicProcessor(settings) }

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicRequest = MssTopic(
                id = MssTopicId("123"),
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(MssState.FINISHING, ctx.state)
        assertEquals(initTopic.id, ctx.topicResponse.id)
        assertEquals(initTopic.title, ctx.topicResponse.title)
        assertEquals(initTopic.description, ctx.topicResponse.description)
        assertEquals(initTopic.status, ctx.topicResponse.status)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
