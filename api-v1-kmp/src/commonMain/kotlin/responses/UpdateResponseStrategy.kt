package ru.mss.api.v1.responses

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IResponse
import ru.mss.api.v1.models.TopicUpdateResponse
import kotlin.reflect.KClass

object UpdateResponseStrategy: IResponseStrategy {
    override val discriminator: String = "update"
    override val clazz: KClass<out IResponse> = TopicUpdateResponse::class
    override val serializer: KSerializer<out IResponse> = TopicUpdateResponse.serializer()
    override fun <T : IResponse> fillDiscriminator(req: T): T {
        require(req is TopicUpdateResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}
