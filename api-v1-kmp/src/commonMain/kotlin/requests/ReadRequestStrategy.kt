package ru.mss.api.v1.requests

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.TopicReadRequest
import kotlin.reflect.KClass

object ReadRequestStrategy: IRequestStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IRequest> = TopicReadRequest::class
    override val serializer: KSerializer<out IRequest> = TopicReadRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is TopicReadRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}
