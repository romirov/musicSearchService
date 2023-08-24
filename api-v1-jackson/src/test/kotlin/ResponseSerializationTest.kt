package ru.mss.api.v1

import ru.mss.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = TopicCreateResponse(
        requestId = "123",
        topic = TopicResponseObject(
            title = "topic title",
            description = "topic description",
            topicType = TopicSide.QUESTIONER,
            status = TopicStatus.OPENED,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"topic title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as TopicCreateResponse

        assertEquals(response, obj)
    }
}
