package ru.mss.api.v1

import ru.mss.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = TopicCreateRequest(
        requestId = "123",
        debug = TopicDebug(
            mode = TopicRequestDebugMode.STUB,
            stub = TopicRequestDebugStubs.BAD_TITLE
        ),
        topic = TopicCreateObject(
            title = "topic title",
            description = "topic description",
            topicType = TopicSide.QUESTIONER,
            status = TopicStatus.OPENED,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"topic title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as TopicCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TopicCreateRequest::class.java)

        assertEquals("123", obj.requestId)
    }
}
