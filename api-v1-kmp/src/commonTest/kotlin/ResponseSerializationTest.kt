package ru.mss.api.v1

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.mss.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response: IResponse = TopicCreateResponse(
        responseType = "create",
        requestId = "123",
        topic = TopicResponseObject(
            title = "topic title",
            description = "topic description",
            status = TopicStatus.OPENED,
        )
    )

    @Test
    fun serialize() {
//        val json = apiV2Mapper.encodeToString(AdRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV1Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"topic title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(response)
//        val json = apiV2Mapper.encodeToString(AdRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
//        val obj = apiV2Mapper.decodeFromString(AdRequestSerializer, json) as AdCreateRequest
        val obj = apiV1Mapper.decodeFromString(json) as TopicCreateResponse

        assertEquals(response, obj)
    }
}
