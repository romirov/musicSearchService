package ru.mss.api.v1.requests

import ru.mss.api.v1.TopicRequestSerializer
import ru.mss.api.v1.apiV1Mapper
import ru.mss.api.v1.models.IRequest

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.encodeToString(TopicRequestSerializer, request)

@Suppress("UNCHECKED_CAST")
fun <T : Any> apiV1RequestDeserialize(json: String): T = apiV1Mapper.decodeFromString(TopicRequestSerializer, json) as T
