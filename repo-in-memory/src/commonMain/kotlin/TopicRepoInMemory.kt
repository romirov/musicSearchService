package ru.mss.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import ru.mss.common.models.*
import ru.mss.common.repo.*
import ru.mss.repo.inmemory.model.TopicEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TopicRepoInMemory(
    initObjects: Collection<MssTopic> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ITopicRepository {

    private val cache = Cache.Builder<String, TopicEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(topic: MssTopic) {
        val entity = TopicEntity(topic)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse {
        val key = randomUuid()
        val topic = rq.topic.copy(id = MssTopicId(key))
        val entity = TopicEntity(topic)
        cache.put(key, entity)
        return DbTopicResponse(
            data = topic,
            isSuccess = true,
        )
    }

    override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse {
        val key = rq.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbTopicResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse {
        val key = rq.topic.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        val newTopic = rq.topic.copy()
        val entity = TopicEntity(newTopic)
        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                DbTopicResponse(
                    data = newTopic,
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse {
        val key = rq.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        return when (val oldTopic = cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.invalidate(key)
                DbTopicResponse(
                    data = oldTopic.toInternal(),
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MssUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != MssTopicStatus.NONE }?.let {
                    it.name == entry.value.status
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbTopicsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbTopicResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MssError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbTopicResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MssError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}