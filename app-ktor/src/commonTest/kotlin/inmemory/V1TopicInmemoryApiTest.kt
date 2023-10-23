package ru.mss.app.ktor.inmemory

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.app.ktor.module
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicStatus
import ru.mss.stubs.MssTopicStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1TopicInmemoryApiTest{
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"
    private val initTopic = MssTopicStub.prepareResult {
        id = MssTopicId(uuidOld)
        title = "abc"
        description = "abc"
        status = MssTopicStatus.OPENED
    }

    @Test
    fun create() = testApplication {
        val repo = TopicRepoInMemory(randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val createTopic = TopicCreateObject(
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED
        )

        val response = client.post("/v1/topic/create") {
            val requestObj = TopicCreateRequest(
                requestId = "1234",
                topic = createTopic,
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicCreateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.topic?.id)
        assertEquals(createTopic.title, responseObj.topic?.title)
        assertEquals(createTopic.description, responseObj.topic?.description)
        assertEquals(createTopic.status, responseObj.topic?.status)
    }

    @Test
    fun read() = testApplication {
        val repo = TopicRepoInMemory(initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val response = client.post("/v1/topic/read") {
            val requestObj = TopicReadRequest(
                requestId = "1234",
                topic = TopicReadObject(uuidOld),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.topic?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = TopicRepoInMemory(initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val topicUpdate = TopicUpdateObject(
            id = uuidOld,
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED,
        )

        val response = client.post("/v1/topic/update") {
            val requestObj = TopicUpdateRequest(
                requestId = "1234",
                topic = topicUpdate,
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(topicUpdate.id, responseObj.topic?.id)
        assertEquals(topicUpdate.title, responseObj.topic?.title)
        assertEquals(topicUpdate.description, responseObj.topic?.description)
        assertEquals(topicUpdate.status, responseObj.topic?.status)
    }

    @Test
    fun delete() = testApplication {
        val repo = TopicRepoInMemory(initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val response = client.post("/v1/topic/delete") {
            val requestObj = TopicDeleteRequest(
                requestId = "1234",
                topic = TopicDeleteObject(
                    id = uuidOld,
                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.topic?.id)
    }

    @Test
    fun search() = testApplication {
        val repo = TopicRepoInMemory(initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val response = client.post("/v1/topic/search") {
            val requestObj = TopicSearchRequest(
                requestId = "12345",
                topicFilter = TopicSearchFilter(),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV1Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV1Mapper.decodeFromString<TopicSearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.topics?.size)
        assertEquals(uuidOld, responseObj.topics?.first()?.id)
    }
}