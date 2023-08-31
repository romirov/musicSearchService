package ru.mss.api.v1.responses

import ru.mss.api.v1.TopicResponseSerializer
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.IResponse

fun apiV1ResponseSerialize(Response: IResponse): String = apiV1Mapper.encodeToString(TopicResponseSerializer, Response)

@Suppress("UNCHECKED_CAST")
fun <T : Any> apiV1ResponseDeserialize(json: String): T = apiV1Mapper.decodeFromString(TopicResponseSerializer, json) as T
