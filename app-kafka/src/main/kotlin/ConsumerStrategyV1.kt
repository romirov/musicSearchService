package ru.mss.app.kafka

import ru.mss.api.v1.apiV1RequestDeserialize
import ru.mss.api.v1.apiV1ResponseSerialize
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.IResponse
import ru.mss.common.MssContext
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic


class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MssContext): String {
        val response: IResponse = source.toTransportTopic()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MssContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}