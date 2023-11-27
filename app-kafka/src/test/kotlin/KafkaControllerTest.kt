package ru.mss.app.kafka

import junit.framework.TestCase.assertEquals
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import ru.mss.api.v1.apiV1RequestSerialize
import ru.mss.api.v1.apiV1ResponseDeserialize
import ru.mss.api.v1.models.*
import java.util.*
import kotlin.test.Test


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        TopicCreateRequest(
                            requestId = "11111111-1111-1111-1111-111111111111",
                            topic = TopicCreateObject(
                                title = "Some Topic",
                                description = "some testing topic to check them all",
                                status = TopicStatus.OPENED
                            ),
                            debug = TopicDebug(
                                mode = TopicRequestDebugMode.STUB,
                                stub = TopicRequestDebugStubs.SUCCESS
                            )
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TopicCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("Some Topic", result.topic?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


