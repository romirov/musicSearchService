package ru.mss.api.v1.requests

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.TopicDeleteRequest
import kotlin.reflect.KClass

object DeleteRequestStrategy: IRequestStrategy {
    override val discriminator: String = "delete"
    override val clazz: KClass<out IRequest> = TopicDeleteRequest::class
    override val serializer: KSerializer<out IRequest> = TopicDeleteRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is TopicDeleteRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}
