package ru.mss.api.v1

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.mss.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request: IRequest = TopicCreateRequest(
        requestType = "create",
        requestId = "123",
        debug = TopicDebug(
            mode = TopicRequestDebugMode.STUB,
            stub = TopicRequestDebugStubs.BAD_TITLE
        ),
        topic = TopicCreateObject(
            title = "topic title",
            description = "topic description",
            status = TopicStatus.OPENED,
        )
    )

    @Test
    fun serialize() {
//        val json = apiV2Mapper.encodeToString(AdRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV1Mapper.encodeToString(request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"topic title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(request)
//        val json = apiV1Mapper.encodeToString(TopicRequestSerializer1, request)
//        val json = apiV1Mapper.encodeToString(RequestSerializers.create, request)
//        val obj = apiV1Mapper.decodeFromString(TopicRequestSerializer, json) as TopicCreateRequest
        val obj = apiV1Mapper.decodeFromString(json) as TopicCreateRequest

        assertEquals(request, obj)
    }
    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "123"}
        """.trimIndent()
        val obj = apiV1Mapper.decodeFromString<TopicCreateRequest>(jsonString)

        assertEquals("123", obj.requestId)
    }
}
