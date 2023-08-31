package ru.mss.api.v1.responses

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IResponse
import ru.mss.api.v1.models.TopicReadResponse
import kotlin.reflect.KClass

object ReadResponseStrategy: IResponseStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IResponse> = TopicReadResponse::class
    override val serializer: KSerializer<out IResponse> = TopicReadResponse.serializer()
    override fun <T : IResponse> fillDiscriminator(req: T): T {
        require(req is TopicReadResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}
