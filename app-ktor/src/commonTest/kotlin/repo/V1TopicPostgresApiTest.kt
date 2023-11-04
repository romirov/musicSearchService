package ru.mss.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.app.ktor.module
import ru.mss.app.ktor.repo.SqlTestCompanion.repoUnderTestContainer
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssTopicAnswerId
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicLock
import ru.mss.common.models.MssTopicStatus
import ru.mss.stubs.MssTopicStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1TopicPostgresApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val initTopic = MssTopicStub.prepareResult {
        id = MssTopicId(uuidOld)
        title = "abc"
        description = "abc"
        status = MssTopicStatus.OPENED
        lock = MssTopicLock(uuidOld)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun tearUp() {
            SqlTestCompanion.start()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            SqlTestCompanion.stop()
        }
    }

    @Test
    fun create() = testApplication {
//        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        val repo = repoUnderTestContainer(test = "create", initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val createTopic = TopicCreateObject(
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED,
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
        assertEquals(uuidNew, responseObj.topic?.id)
        assertEquals(createTopic.title, responseObj.topic?.title)
        assertEquals(createTopic.description, responseObj.topic?.description)
        assertEquals(createTopic.status, responseObj.topic?.status)
    }

    @Test
    fun read() = testApplication {
//        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        val repo = repoUnderTestContainer(test = "read", initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/read") {
            val requestObj = TopicReadRequest(
                requestId = "12345",
                topic = TopicReadObject(uuidOld),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.topic?.id)
    }

    @Test
    fun update() = testApplication {
//        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        val repo = repoUnderTestContainer(test = "update", initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val topicUpdate = TopicUpdateObject(
            id = uuidOld,
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED,
            answer = Answer(
                id = uuidNew,
                userId = "123",
                answerBody = "Моцарт"
            ),
            lock = initTopic.lock.asString(),
        )

        val response = client.post("/v1/topic/update") {
            val requestObj = TopicUpdateRequest(
                requestId = "12345",
                topic = topicUpdate,
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
//        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        val repo = repoUnderTestContainer(test = "delete", initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/delete") {
            val requestObj = TopicDeleteRequest(
                requestId = "12345",
                topic = TopicDeleteObject(
                    id = uuidOld,
                    lock = initTopic.lock.asString(),
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
        assertEquals(uuidOld, responseObj.topic?.id)
    }

    @Test
    fun search() = testApplication {
//        val repo = AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
        val repo = repoUnderTestContainer(test = "search", initObjects = listOf(initTopic), randomUuid = { uuidNew })
        application {
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
        assertEquals(uuidOld, responseObj.topics?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(json = apiV1Mapper)
        }
    }
}
