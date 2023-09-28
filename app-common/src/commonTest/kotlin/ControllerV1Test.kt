package ru.mss.app.common

import kotlinx.coroutines.test.runTest
import ru.mss.api.v1.models.*
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV1Test {

    private val request = TopicCreateRequest(
        requestType = "create",
        requestId = "1234",
        topic = TopicCreateObject(
            title = "some topic",
            description = "some description of some topic",
            status = TopicStatus.OPENED,
        ),
        debug = TopicDebug(mode = TopicRequestDebugMode.STUB, stub = TopicRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IMssAppSettings = object : IMssAppSettings {
        override val corSettings: MssCorSettings = MssCorSettings()
        override val processor: MssTopicProcessor = MssTopicProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createTopicKtor(appSettings: IMssAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TopicCreateRequest>()) },
            { toTransportTopic() },
            this::class,
            "createTopicKtor",
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createTopicKtor(appSettings) }
        val res = testApp.res as TopicCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}