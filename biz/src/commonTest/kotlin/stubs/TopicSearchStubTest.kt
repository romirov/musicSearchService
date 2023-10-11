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
import kotlin.test.assertTrue
import kotlin.test.fail


@OptIn(ExperimentalCoroutinesApi::class)
class TopicSearchStubTest {

    private val processor = MssTopicProcessor()
    val filter = MssTopicFilter(searchString = "Artist`s name")

    @Test
    fun read() = runTest {

        val ctx = MssContext(
            command = MssCommand.SEARCH,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.SUCCESS,
            topicFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.topicsResponse.size > 1)
        val first = ctx.topicsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (MssTopicStub.get()) {
            assertEquals(status, first.status)
            assertEquals(answers, first.answers)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MssContext(
            command = MssCommand.SEARCH,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_ID,
            topicFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MssContext(
            command = MssCommand.SEARCH,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.DB_ERROR,
            topicFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MssContext(
            command = MssCommand.SEARCH,
            state = MssState.NONE,
            workMode = MssWorkMode.STUB,
            stubCase = MssStubs.BAD_TITLE,
            topicFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MssTopic(), ctx.topicResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}