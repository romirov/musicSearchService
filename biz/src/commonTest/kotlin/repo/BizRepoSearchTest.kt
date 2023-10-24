package ru.mss.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.*
import ru.mss.common.repo.DbTopicsResponse
import ru.mss.repo.tests.TopicRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = MssUserId("321")
    private val command = MssCommand.SEARCH
    private val initTopic = MssTopic(
        id = MssTopicId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        status = MssTopicStatus.OPENED
    )
    private val repo by lazy {
        TopicRepositoryMock(
            invokeSearchTopic = {
                DbTopicsResponse(
                    isSuccess = true,
                    data = listOf(initTopic),
                )
            }
        )
    }
    private val settings by lazy {
        MssCorSettings(repoTest = repo)
    }
    private val processor by lazy { MssTopicProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicFilterRequest = MssTopicFilter(searchString = "ab"),
        )
        processor.exec(ctx)
        assertEquals(MssState.FINISHING, ctx.state)
        assertEquals(1, ctx.topicsResponse.size)
    }
}
