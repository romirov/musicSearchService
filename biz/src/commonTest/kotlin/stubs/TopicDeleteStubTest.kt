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
class TopicDeleteStubTest {

    private val processor = MssTopicProcessor()
    val id = MssTopicId("666")

    @Test
    fun delete() = runTest {

        val ctx = MssContext(
            command = MssCommand.DELETE,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.SUCCESS,
            topicRequest = MssTopic(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = MssTopicStub.get()
        assertEquals(stub.id, ctx.topicResponse.id)
        assertEquals(stub.title, ctx.topicResponse.title)
        assertEquals(stub.description, ctx.topicResponse.description)
        assertEquals(stub.status, ctx.topicResponse.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = MssContext(
            command = MssCommand.DELETE,
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
            command = MssCommand.DELETE,
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
            command = MssCommand.DELETE,
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