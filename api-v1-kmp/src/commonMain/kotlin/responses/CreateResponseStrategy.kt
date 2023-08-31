package ru.mss.api.v1.responses

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IResponse
import ru.mss.api.v1.models.TopicCreateResponse
import kotlin.reflect.KClass

object CreateResponseStrategy: IResponseStrategy {
    override val discriminator: String = "create"
    override val clazz: KClass<out IResponse> = TopicCreateResponse::class
    override val serializer: KSerializer<out IResponse> = TopicCreateResponse.serializer()
    override fun <T : IResponse> fillDiscriminator(req: T): T {
        require(req is TopicCreateResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}
