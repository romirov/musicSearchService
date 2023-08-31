package ru.mss.mappers.v1

import ru.mss.api.v1.models.*
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.common.stubs.MssStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = TopicCreateRequest(
            requestId = "1234",
            debug = TopicDebug(
                mode = TopicRequestDebugMode.STUB,
                stub = TopicRequestDebugStubs.SUCCESS,
            ),
            topic = TopicCreateObject(
                title = "title",
                description = "desc",
                status = TopicStatus.OPENED
            ),
        )

        val context = MssContext()
        context.fromTransport(req)

        assertEquals(MssStubs.SUCCESS, context.stubCase)
        assertEquals(MssWorkMode.STUB, context.workMode)
        assertEquals("title", context.topicRequest.title)
        assertEquals(MssTopicStatus.OPENED, context.topicRequest.status)
    }

    @Test
    fun toTransport() {
        val context = MssContext(
            requestId = MssRequestId("1234"),
            command = MssCommand.CREATE,
            topicResponse = MssTopic(
                title = "title",
                description = "desc",
                status = MssTopicStatus.OPENED
            ),
            errors = mutableListOf(
                MssError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MssState.RUNNING,
        )

        val req = context.toTransportTopic() as TopicCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.topic?.title)
        assertEquals("desc", req.topic?.description)
        assertEquals(TopicStatus.OPENED, req.topic?.status)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
