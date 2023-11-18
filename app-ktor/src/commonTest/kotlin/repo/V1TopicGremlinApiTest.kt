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
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.RemoteDockerImage
import org.testcontainers.utility.DockerImageName
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.app.ktor.module
import ru.mss.app.ktor.moduleJvm
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicLock
import ru.mss.common.models.MssTopicStatus
import ru.mss.repo.gremlin.TopicRepoGremlin
import ru.mss.stubs.MssTopicStub
import java.time.Duration
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1TopicGremlinApiTest {

    @Test
    fun create() = testApplication {
//        val repo = repoUnderTestContainer(test = "create", initObjects = listOf(initAd), randomUuid = { uuidNew })
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
        assertEquals(createTopic.title, responseObj.topic?.title)
        assertEquals(createTopic.description, responseObj.topic?.description)
        assertEquals(createTopic.status, responseObj.topic?.status)
        assertEquals(uuidNew, responseObj.topic?.lock)
    }

    @Test
    fun read() = testApplication {
//        val repo = repoUnderTestContainer(test = "read", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/read") {
            val requestObj = TopicReadRequest(
                requestId = "12345",
                topic = TopicReadObject(initTopic.id.asString()),
                debug = TopicDebug(
                    mode = TopicRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<TopicReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(initTopic.id.asString(), responseObj.topic?.id)
    }

    @Test
    fun update() = testApplication {
//        val repo = repoUnderTestContainer(test = "update", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            module(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val topicUpdate = TopicUpdateObject(
            id = initTopic.id.asString(),
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            status = TopicStatus.OPENED,
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
        application {
            moduleJvm(MssAppSettings(corSettings = MssCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/topic/delete") {
            val requestObj = TopicDeleteRequest(
                requestId = "12345",
                topic = TopicDeleteObject(
                    id = initTopicDelete.id.asString(),
                    lock = initTopicDelete.lock.asString(),
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
        assertEquals(initTopicDelete.id.asString(), responseObj.topic?.id)
    }

    @Test
    fun search() = testApplication {
//        val repo = repoUnderTestContainer(test = "search", initObjects = listOf(initAd), randomUuid = { uuidNew })
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
        assertContains(responseObj.topics?.map { it.id }?: emptyList(), initTopic.id.asString())
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(json = apiV1Mapper)
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun tearUp() {
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.stop()
        }

        private const val USER = "root"
        private const val PASS = "musicsearchservice-pass"

        private val container by lazy {
            GenericContainer(RemoteDockerImage(DockerImageName.parse("arcadedata/arcadedb:23.10.1"))).apply {
                withExposedPorts(2480, 2424, 8182)
                withEnv(
                    "JAVA_OPTS",
                    "-Darcadedb.server.rootPassword=$PASS " +
                            "-Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin"
                )
                waitingFor(Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
                withStartupTimeout(Duration.ofMinutes(5))
                start()
            }
        }

        private val uuidOld = "10000000-0000-0000-0000-000000000001"
        private val uuidNew = "10000000-0000-0000-0000-000000000002"

        private val initObjects = listOf(
            MssTopicStub.prepareResult {
                id = MssTopicId.NONE
                title = "abc"
                description = "abc"
                status = MssTopicStatus.OPENED
                lock = MssTopicLock(uuidOld)
            },
            MssTopicStub.prepareResult {
                id = MssTopicId.NONE
                title = "delete"
                description = "delete"
                status = MssTopicStatus.OPENED
                lock = MssTopicLock(uuidOld)
            },
            MssTopicStub.prepareResult {
                id = MssTopicId.NONE
                title = "abc"
                description = "abc"
                status = MssTopicStatus.OPENED
            }
        )
        private val repo by lazy {
            TopicRepoGremlin(
                hosts = container.host,
                user = USER,
                pass = PASS,
                port = container.getMappedPort(8182),
                initObjects = initObjects,
                randomUuid = { uuidNew }
            )
        }

        private val initTopic = repo.initializedObjects.first()
        private val initTopicDelete = repo.initializedObjects[1]
        private val initTopicSupply = repo.initializedObjects.last()
    }
}
