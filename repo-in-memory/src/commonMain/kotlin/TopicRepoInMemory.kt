package ru.mss.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.mss.common.helpers.errorRepoConcurrency
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
    private val mutex: Mutex = Mutex()

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
        val topic = rq.topic.copy(id = MssTopicId(key), lock = MssTopicLock(randomUuid()))
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

    private suspend fun doUpdate(
        id: MssTopicId,
        oldLock: MssTopicLock,
        okBlock: (key: String, oldAd: TopicEntity) -> DbTopicResponse
    ): DbTopicResponse {
        val key = id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockStr = oldLock.takeIf { it != MssTopicLock.NONE }?.asString()
            ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldTopic = cache.get(key)
            when {
                oldTopic == null -> resultErrorNotFound
                oldTopic.lock != oldLockStr -> DbTopicResponse.errorConcurrent(
                    oldLock,
                    oldTopic.toInternal()
                )

                else -> okBlock(key, oldTopic)
            }
        }
    }

    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse =
        doUpdate(rq.topic.id, rq.topic.lock) { key, _ ->
            val newTopic = rq.topic.copy(lock = MssTopicLock(randomUuid()))
            val entity = TopicEntity(newTopic)
            cache.put(key, entity)
            DbTopicResponse.success(newTopic)
        }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse =
        doUpdate(rq.id, rq.lock) { key, oldTopic ->
            cache.invalidate(key)
            DbTopicResponse.success(oldTopic.toInternal())
        }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.searchString.takeIf { it.isNotBlank() }?.let {
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
        val resultErrorEmptyLock = DbTopicResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MssError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
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