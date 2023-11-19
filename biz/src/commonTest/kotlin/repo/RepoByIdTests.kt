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
import kotlin.test.assertEquals

private val initTopic = MssTopic(
    id = MssTopicId("123"),
    title = "abc",
    description = "abc",
    status = MssTopicStatus.OPENED,
)
private val repo = TopicRepositoryMock(
    invokeReadTopic = {
        if (it.id == initTopic.id) {
            DbTopicResponse(
                isSuccess = true,
                data = initTopic,
            )
        } else DbTopicResponse(
            isSuccess = false,
            data = null,
            errors = listOf(MssError(message = "Not found", field = "id"))
        )
    }
)
private val settings by lazy {
    MssCorSettings(
        repoTest = repo
    )
}
private val processor by lazy { MssTopicProcessor(settings) }

fun repoNotFoundTest(command: MssCommand) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("12345"),
            title = "xyz",
            description = "xyz",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(MssState.FAILING, ctx.state)
    assertEquals(MssTopic(), ctx.topicResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
