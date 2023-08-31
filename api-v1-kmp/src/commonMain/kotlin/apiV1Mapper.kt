package ru.mss.api.v1

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import ru.mss.api.v1.models.*

@OptIn(ExperimentalSerializationApi::class)
val apiV1Mapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        polymorphicDefaultSerializer(IRequest::class) {
            @Suppress("UNCHECKED_CAST")
            when(it) {
                is TopicCreateRequest ->  RequestSerializer(TopicCreateRequest.serializer()) as SerializationStrategy<IRequest>
                is TopicReadRequest   ->  RequestSerializer(TopicReadRequest  .serializer()) as SerializationStrategy<IRequest>
                is TopicUpdateRequest ->  RequestSerializer(TopicUpdateRequest.serializer()) as SerializationStrategy<IRequest>
                is TopicDeleteRequest ->  RequestSerializer(TopicDeleteRequest.serializer()) as SerializationStrategy<IRequest>
                is TopicSearchRequest ->  RequestSerializer(TopicSearchRequest.serializer()) as SerializationStrategy<IRequest>
                else -> null
            }
        }
        polymorphicDefault(IRequest::class) {
            TopicRequestSerializer
        }
        polymorphicDefaultSerializer(IResponse::class) {
            @Suppress("UNCHECKED_CAST")
            when(it) {
                is TopicCreateResponse ->  ResponseSerializer(TopicCreateResponse.serializer()) as SerializationStrategy<IResponse>
                is TopicReadResponse   ->  ResponseSerializer(TopicReadResponse  .serializer()) as SerializationStrategy<IResponse>
                is TopicUpdateResponse ->  ResponseSerializer(TopicUpdateResponse.serializer()) as SerializationStrategy<IResponse>
                is TopicDeleteResponse ->  ResponseSerializer(TopicDeleteResponse.serializer()) as SerializationStrategy<IResponse>
                is TopicSearchResponse ->  ResponseSerializer(TopicSearchResponse.serializer()) as SerializationStrategy<IResponse>
                is TopicInitResponse   ->  ResponseSerializer(TopicInitResponse  .serializer()) as SerializationStrategy<IResponse>
                else -> null
            }
        }
        polymorphicDefault(IResponse::class) {
            TopicResponseSerializer
        }

        contextual(TopicRequestSerializer)
        contextual(TopicResponseSerializer)
    }
}

fun Json.encodeResponse(response: IResponse): String = encodeToString(TopicResponseSerializer, response)

fun apiV1ResponseSerialize(Response: IResponse): String = apiV1Mapper.encodeToString(TopicResponseSerializer, Response)

@Suppress("UNCHECKED_CAST")
fun <T : Any> apiV1ResponseDeserialize(json: String): T = apiV1Mapper.decodeFromString(TopicResponseSerializer, json) as T

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.encodeToString(TopicRequestSerializer, request)

@Suppress("UNCHECKED_CAST")
fun <T : Any> apiV1RequestDeserialize(json: String): T = apiV1Mapper.decodeFromString(TopicRequestSerializer, json) as T
