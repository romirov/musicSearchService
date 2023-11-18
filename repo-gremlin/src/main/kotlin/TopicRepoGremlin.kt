package ru.mss.repo.gremlin

import com.benasher44.uuid.uuid4
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import ru.mss.common.helpers.asMssError
import ru.mss.common.helpers.errorAdministration
import ru.mss.common.helpers.errorRepoConcurrency
import ru.mss.common.models.*
import ru.mss.common.repo.*
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_AD_TYPE
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_LOCK
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_OWNER_ID
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_TITLE
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_TMP_RESULT
import ru.mss.repo.gremlin.TopicGremlinConst.RESULT_LOCK_FAILURE
import ru.mss.repo.gremlin.exceptions.DbDuplicatedElementsException
import ru.mss.repo.gremlin.mappers.addMssTopic
import ru.mss.repo.gremlin.mappers.label
import ru.mss.repo.gremlin.mappers.listMssTopic
import ru.mss.repo.gremlin.mappers.toMssTopic
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr


class TopicRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    initObjects: Collection<MssTopic> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },
) : ITopicRepository {

    val initializedObjects: List<MssTopic>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(topic: MssTopic): MssTopic = g.addV(topic.label())
        .addMssTopic(topic)
        .listMssTopic()
        .next()
        ?.toMssTopic()
        ?: throw RuntimeException("Cannot initialize object $topic")

    override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse {
        val key = randomUuid()
        val ad = rq.topic.copy(id = MssTopicId(key), lock = MssTopicLock(randomUuid()))
        val dbRes = try {
            g.addV(ad.label())
                .addMssTopic(ad)
                .listMssTopic()
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbTopicResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asMssError())
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbTopicResponse(
                data = dbRes.first().toMssTopic(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse {
        val key = rq.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = try {
            g.V(key).listMssTopic().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbTopicResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asMssError())
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbTopicResponse(
                data = dbRes.first().toMssTopic(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse {
        val key = rq.topic.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.topic.lock.takeIf { it != MssTopicLock.NONE } ?: return resultErrorEmptyLock
        val newLock = MssTopicLock(randomUuid())
        val newAd = rq.topic.copy(lock = newLock)
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a").addMssTopic(newAd).listMssTopic(),
                    gr.select<Vertex, Vertex>("a").listMssTopic(result = RESULT_LOCK_FAILURE)
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbTopicResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asMssError())
            )
        }
        val adResult = dbRes.firstOrNull()?.toMssTopic()
        return when {
            adResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            adResult.lock != newLock -> DbTopicResponse(
                data = adResult,
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock,
                        actualLock = adResult.lock,
                    ),
                )
            )

            else -> DbTopicResponse(
                data = adResult,
                isSuccess = true,
            )
        }
    }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse {
        val key = rq.id.takeIf { it != MssTopicId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != MssTopicLock.NONE } ?: return resultErrorEmptyLock
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .listMssTopic(),
                    gr.select<Vertex, Vertex>("a")
                        .listMssTopic(result = RESULT_LOCK_FAILURE)
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbTopicResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asMssError())
            )
        }
        val dbFirst = dbRes.firstOrNull()
        val adResult = dbFirst?.toMssTopic()
        return when {
            adResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            dbFirst[FIELD_TMP_RESULT] == RESULT_LOCK_FAILURE -> DbTopicResponse(
                data = adResult,
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock,
                        actualLock = adResult.lock,
                    ),
                )
            )

            else -> DbTopicResponse(
                data = adResult,
                isSuccess = true,
            )
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
        val result = try {
            g.V()
                .apply { rq.ownerId.takeIf { it != MssUserId.NONE }?.also { has(FIELD_OWNER_ID, it.asString()) } }
                .apply { rq.dealSide.takeIf { it != MssDealSide.NONE }?.also { has(FIELD_AD_TYPE, it.name) } }
                .apply {
                    rq.titleFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) }
                }
                .listMssTopic()
                .toList()
        } catch (e: Throwable) {
            return DbTopicsResponse(
                isSuccess = false,
                data = null,
                errors = listOf(e.asMssError())
            )
        }
        return DbTopicsResponse(
            data = result.map { it.toMssTopic() },
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbTopicResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MssError(
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
                    field = "lock",
                    message = "Lock must be provided"
                )
            )
        )

        fun resultErrorNotFound(key: String, e: Throwable? = null) = DbTopicResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MssError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found object with key $key",
                    exception = e
                )
            )
        )

        fun errorDuplication(key: String) = DbTopicResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                errorAdministration(
                    violationCode = "duplicateObjects",
                    description = "Database consistency failure",
                    exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                )
            )
        )
    }
}
