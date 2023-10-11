package ru.mss.biz.stubs

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.common.stubs.MssStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TopicUpdateStubTest {

    private val processor = MssTopicProcessor()
    val id = MssTopicId("777")
    val title = "title 666"
    val description = "desc 666"
    val status = MssTopicStatus.OPENED

    @Test
    fun create() = runTest {

        val ctx = MssContext(
            command = MssCommand.UPDATE,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.SUCCESS,
            topicRequest = MssTopic(
                id = id,
                title = title,
                description = description,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.topicResponse.id)
        assertEquals(title, ctx.topicResponse.title)
        assertEquals(description, ctx.topicResponse.description)
        assertEquals(status, ctx.topicResponse.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = MssContext(
            command = MssCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = MssContext(
            command = MssCommand.UPDATE,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_TITLE,
            topicRequest = MssTopic(
                id = id,
                title = "",
                description = description,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MssContext(
            command = MssCommand.UPDATE,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_DESCRIPTION,
            topicRequest = MssTopic(
                id = id,
                title = title,
                description = "",
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MssContext(
            command = MssCommand.UPDATE,
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
            command = MssCommand.UPDATE,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_SEARCH_STRING,
            topicRequest = MssTopic(
                id = id,
                title = title,
                description = description,
                status = status,
            ),
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}