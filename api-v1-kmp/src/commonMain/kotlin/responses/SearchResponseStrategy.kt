package ru.mss.api.v1.responses

import kotlinx.serialization.KSerializer
import ru.mss.api.v1.models.IResponse
import ru.mss.api.v1.models.TopicSearchResponse
import kotlin.reflect.KClass

object SearchResponseStrategy: IResponseStrategy {
    override val discriminator: String = "search"
    override val clazz: KClass<out IResponse> = TopicSearchResponse::class
    override val serializer: KSerializer<out IResponse> = TopicSearchResponse.serializer()
    override fun <T : IResponse> fillDiscriminator(req: T): T {
        require(req is TopicSearchResponse)
        @Suppress("UNCHECKED_CAST")
        return req.copy(responseType = discriminator) as T
    }
}
