package ru.mss.api.v1.requests

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.TopicUpdateRequest
import kotlin.reflect.KClass

object UpdateRequestStrategy: IRequestStrategy {
    override val discriminator: String = "update"
    override val clazz: KClass<out IRequest> = TopicUpdateRequest::class
    override val serializer: KSerializer<out IRequest> = TopicUpdateRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is TopicUpdateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}
