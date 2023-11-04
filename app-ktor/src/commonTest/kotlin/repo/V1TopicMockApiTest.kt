package ru.mss.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.app.ktor.module
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssTopic
import ru.mss.common.repo.DbTopicResponse
import ru.mss.common.repo.DbTopicsResponse
import ru.mss.repo.tests.TopicRepositoryMock
import ru.mss.stubs.MssTopicStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1TopicMockApiTest {
    private val stub = MssTopicStub.get()
    private val userId = stub.ownerId
    private val topicId = stub.id

    @Test
    fun create() = testApplication {
        val repo = TopicRepositoryMock(
            invokeCreateTopic = {
                DbTopicResponse(
                    isSuccess = true,
                    data = it.topic.copy(id = topicId),
                )
            }
        )
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val createTopic = TopicCreateObject(
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED
        )

        val response = client.post("/v1/topic/create") {
            val requestObj = TopicCreateRequest(
                requestId = "12345",
                topic = createTopic,
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(topicId.asString(), responseObj.topic?.id)
        assertEquals(createTopic.title, responseObj.topic?.title)
        assertEquals(createTopic.description, responseObj.topic?.description)
        assertEquals(createTopic.status, responseObj.topic?.status)
    }

    @Test
    fun read() = testApplication {
        val repo = TopicRepositoryMock(
            invokeReadTopic = {
                DbTopicResponse(
                    isSuccess = true,
                    data = MssTopic(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            }
        )
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/read") {
            val requestObj = TopicReadRequest(
                requestId = "12345",
                topic = TopicReadObject(topicId.asString()),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(topicId.asString(), responseObj.topic?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = TopicRepositoryMock(
            invokeReadTopic = {
                DbTopicResponse(
                    isSuccess = true,
                    data = MssTopic(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            },
            invokeUpdateTopic = {
                DbTopicResponse(
                    isSuccess = true,
                    data = it.topic.copy(ownerId = userId),
                )
            }
        )
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val topicUpdate = TopicUpdateObject(
            id = "666",
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED,
            lock = "123",
        )

        val response = client.post("/v1/topic/update") {
            val requestObj = TopicUpdateRequest(
                requestId = "12345",
                topic = TopicUpdateObject(
                    id = "666",
                    title = "Неизвестная композиция",
                    description = "Неизвестна композиция неизвестного автора",
                    status = TopicStatus.OPENED,
                    lock = "123",
                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(topicUpdate.id, responseObj.topic?.id)
        assertEquals(topicUpdate.title, responseObj.topic?.title)
        assertEquals(topicUpdate.description, responseObj.topic?.description)
        assertEquals(topicUpdate.status, responseObj.topic?.status)
    }

    @Test
    fun delete() = testApplication {
        application {
            val repo = TopicRepositoryMock(
                invokeReadTopic = {
                    DbTopicResponse(
                        isSuccess = true,
                        data = MssTopic(
                            id = it.id,
                            ownerId = userId,
                        ),
                    )
                },
                invokeDeleteTopic = {
                    DbTopicResponse(
                        isSuccess = true,
                        data = MssTopic(
                            id = it.id,
                            ownerId = userId,
                        ),
                    )
                }
            )
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }

        val client = myClient()

        val deleteId = "666"

        val response = client.post("/v1/topic/delete") {
            val requestObj = TopicDeleteRequest(
                requestId = "12345",
                topic = TopicDeleteObject(
                    id = deleteId,
                    lock = "123",
                ),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(deleteId, responseObj.topic?.id)
    }

    @Test
    fun search() = testApplication {
        application {
            val repo =
                TopicRepositoryMock(
                    invokeSearchTopic = {
                        DbTopicsResponse(
                            isSuccess = true,
                            data = listOf(
                                MssTopic(
                                    title = it.searchString,
                                )
                            ),
                        )
                    }
                )
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/search") {
            val requestObj = TopicSearchRequest(
                requestId = "12345",
                topicFilter = TopicSearchFilter(),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.topics?.size)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(json = apiV1Mapper)
        }
    }
}
