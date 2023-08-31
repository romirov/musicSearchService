package ru.mss.api.v1.requests

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.TopicCreateRequest
import kotlin.reflect.KClass

object CreateRequestStrategy: IRequestStrategy {
    override val discriminator: String = "create"
    override val clazz: KClass<out IRequest> = TopicCreateRequest::class
    override val serializer: KSerializer<out IRequest> = TopicCreateRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is TopicCreateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}
