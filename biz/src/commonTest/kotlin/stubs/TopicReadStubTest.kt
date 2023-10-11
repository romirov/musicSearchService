package ru.mss.biz.stubs

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.common.stubs.MssStubs
import ru.mss.stubs.MssTopicStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TopicReadStubTest {

    private val processor = MssTopicProcessor()
    val id = MssTopicId("666")

    @Test
    fun read() = runTest {

        val ctx = MssContext(
            command = MssCommand.READ,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.SUCCESS,
            topicRequest = MssTopic(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (MssTopicStub.get()) {
            assertEquals(id, ctx.topicResponse.id)
            assertEquals(title, ctx.topicResponse.title)
            assertEquals(description, ctx.topicResponse.description)
            assertEquals(status, ctx.topicResponse.status)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MssContext(
            command = MssCommand.READ,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_ID,
            topicRequest = MssTopic(),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MssContext(
            command = MssCommand.READ,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.DB_ERROR,
            topicRequest = MssTopic(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MssContext(
            command = MssCommand.READ,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_TITLE,
            topicRequest = MssTopic(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}