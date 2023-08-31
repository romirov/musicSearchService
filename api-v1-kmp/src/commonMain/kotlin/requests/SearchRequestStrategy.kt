package ru.mss.api.v1.requests

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.TopicSearchRequest
import kotlin.reflect.KClass

object SearchRequestStrategy: IRequestStrategy {
    override val discriminator: String = "search"
    override val clazz: KClass<out IRequest> = TopicSearchRequest::class
    override val serializer: KSerializer<out IRequest> = TopicSearchRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is TopicSearchRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}
