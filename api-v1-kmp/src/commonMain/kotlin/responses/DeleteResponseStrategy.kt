package ru.mss.api.v1.responses

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IResponse
import ru.mss.api.v1.models.TopicDeleteResponse
import kotlin.reflect.KClass

object DeleteResponseStrategy: IResponseStrategy {
    override val discriminator: String = "delete"
    override val clazz: KClass<out IResponse> = TopicDeleteResponse::class
    override val serializer: KSerializer<out IResponse> = TopicDeleteResponse.serializer()
    override fun <T : IResponse> fillDiscriminator(req: T): T {
        require(req is TopicDeleteResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}
