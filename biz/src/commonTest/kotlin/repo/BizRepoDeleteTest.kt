package ru.mss.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.*
import ru.mss.common.repo.DbTopicResponse
import ru.mss.repo.tests.TopicRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoDeleteTest {

    private val userId = MssUserId("321")
    private val command = MssCommand.DELETE
    private val initTopic = MssTopic(
        id = MssTopicId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        status = MssTopicStatus.OPENED,
        lock = MssTopicLock("123-234-abc-ABC"),
    )
    private val repo by lazy {
        TopicRepositoryMock(
            invokeReadTopic = {
               DbTopicResponse(
                   isSuccess = true,
                   data = initTopic,
               )
            },
            invokeDeleteTopic = {
                if (it.id == initTopic.id)
                    DbTopicResponse(
                        isSuccess = true,
                        data = initTopic
                    )
                else DbTopicResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy {
        MssCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { MssTopicProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val topicToUpdate = MssTopic(
            id = MssTopicId("123"),
            lock = MssTopicLock("123-234-abc-ABC"),
        )
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicRequest = topicToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MssState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initTopic.id, ctx.topicResponse.id)
        assertEquals(initTopic.title, ctx.topicResponse.title)
        assertEquals(initTopic.description, ctx.topicResponse.description)
        assertEquals(initTopic.status, ctx.topicResponse.status)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
