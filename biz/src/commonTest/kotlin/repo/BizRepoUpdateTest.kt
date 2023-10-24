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

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {

    private val userId = MssUserId("321")
    private val command = MssCommand.UPDATE
    private val initTopic = MssTopic(
        id = MssTopicId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        status = MssTopicStatus.OPENED
    )
    private val repo by lazy { TopicRepositoryMock(
        invokeReadTopic = {
            DbTopicResponse(
                isSuccess = true,
                data = initTopic,
            )
        },
        invokeUpdateTopic = {
            DbTopicResponse(
                isSuccess = true,
                data = MssTopic(
                    id = MssTopicId("123"),
                    title = "xyz",
                    description = "xyz",
                    status = MssTopicStatus.OPENED
                )
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
    fun repoUpdateSuccessTest() = runTest {
        val topicToUpdate = MssTopic(
            id = MssTopicId("123"),
            title = "xyz",
            description = "xyz",
            status = MssTopicStatus.OPENED
        )
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicRequest = topicToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MssState.FINISHING, ctx.state)
        assertEquals(topicToUpdate.id, ctx.topicResponse.id)
        assertEquals(topicToUpdate.title, ctx.topicResponse.title)
        assertEquals(topicToUpdate.description, ctx.topicResponse.description)
        assertEquals(topicToUpdate.status, ctx.topicResponse.status)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
