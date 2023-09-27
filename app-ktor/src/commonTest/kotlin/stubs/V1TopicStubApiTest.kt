package ru.mss.app.ktor.stubs

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.module
import kotlin.test.Test
import kotlin.test.assertEquals

class V1TopicStubApiTest {

    @Test
    fun create() = testApplication {
        application { module() }
        val response = client.post("/v1/topic/create") {
            val requestObj = TopicCreateRequest(
                requestId = "12345",
                topic = TopicCreateObject(
                    title = "Неизвестная композиция",
                    description = "Неизвестна композиция неизвестного автора",
                    status = TopicStatus.OPENED,
                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.STUB,
                    stub = TopicRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicCreateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.topic?.id)
    }

    @Test
    fun read() = testApplication {
        application { module() }
        val response = client.post("/v1/topic/read") {
            val requestObj = TopicReadRequest(
                requestId = "12345",
                topic = TopicReadObject("666"),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.STUB,
                    stub = TopicRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.topic?.id)
    }

    @Test
    fun update() = testApplication {
        application { module() }
        val response = client.post("/v1/topic/update") {
            val requestObj = TopicUpdateRequest(
                requestId = "12345",
                topic = TopicUpdateObject(
                    id = "666",
                    title = "Неизвестная композиция",
                    description = "Неизвестна композиция неизвестного автора",
                    status = TopicStatus.OPENED,
                    answer = Answer(
                        id = "1",
                        userId = "user-11",
                        answerBody = "предположительно Моцарт"
                    )

                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.STUB,
                    stub = TopicRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.topic?.id)
    }

    @Test
    fun delete() = testApplication {
        application { module() }
        val response = client.post("/v1/topic/delete") {
            val requestObj = TopicDeleteRequest(
                requestId = "12345",
                topic = TopicDeleteObject(
                    id = "666",
                    lock = "123"
                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.STUB,
                    stub = TopicRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.topic?.id)
    }

    @Test
    fun search() = testApplication {
        application { module() }
        val response = client.post("/v1/topic/search") {
            val requestObj = TopicSearchRequest(
                requestId = "12345",
                topicFilter = TopicSearchFilter(),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.STUB,
                    stub = TopicRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicSearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.topics?.first()?.id)
    }
}
