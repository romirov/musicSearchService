package ru.mss.repo.postgresql

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.mss.common.helpers.asMssError
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicLock
import ru.mss.common.repo.*

class RepoTopicSQL(
    properties: SqlProperties,
    initObjects: Collection<MssTopic> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : ITopicRepository {
    private val topicTable = TopicTable(properties.tableTopic)
    private val answerTable = AnswerTable(properties.tableAnswer, topicTable.tableName)

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    init {
        transaction(conn) {
            SchemaUtils.create(topicTable)
            SchemaUtils.create(answerTable)
            initObjects.forEach { createTopic(it) }
        }
    }

    private fun createTopic(topic: MssTopic): MssTopic {
        val res = topicTable
            .insert {
                to(it, topic, randomUuid)
            }
            .resultedValues
            ?.map {
                topicTable.from(it, answerTable)
            }
        return res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction(conn) {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbTopicResponse): DbTopicResponse =
        transactionWrapper(block) { DbTopicResponse.error(it.asMssError()) }

    override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse = transactionWrapper {
        DbTopicResponse.success(createTopic(rq.topic))
    }

    private fun read(id: MssTopicId): DbTopicResponse {
        val res = topicTable.select {
            topicTable.id eq id.asString()
        }.singleOrNull() ?: return DbTopicResponse.errorNotFound
        return DbTopicResponse.success(topicTable.from(res, answerTable))
    }

    override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse = transactionWrapper { read(rq.id) }

    private fun update(
        id: MssTopicId,
        lock: MssTopicLock,
        block: (MssTopic) -> DbTopicResponse
    ): DbTopicResponse =
        transactionWrapper {
            if (id == MssTopicId.NONE) return@transactionWrapper DbTopicResponse.errorEmptyId

            val current = topicTable.select { topicTable.id eq id.asString() }
                .singleOrNull()
                ?.let { topicTable.from(it, answerTable) }

            when {
                current == null -> DbTopicResponse.errorNotFound
                current.lock != lock -> DbTopicResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }


    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse = update(rq.topic.id, rq.topic.lock) {
        topicTable.update({ topicTable.id eq rq.topic.id.asString() }) {
            to(it, rq.topic.copy(lock = MssTopicLock(randomUuid())), randomUuid)
        }
        if (rq.topic.answers.isNotEmpty()) {
            answerTable.insert {
                to(it, rq.topic.id.asString(), rq.topic.answers.last(), randomUuid)
            }
        }
        read(rq.topic.id)
    }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse = update(rq.id, rq.lock) {
        topicTable.deleteWhere { id eq rq.id.asString() }
        answerTable.deleteWhere { topicId eq rq.id.asString() }
        DbTopicResponse.success(it)
    }

    suspend fun deleteAllTopics(): DbTopicResponse = transactionWrapper {
        topicTable.deleteAll()
        answerTable.deleteAll()
        DbTopicResponse(data = null, isSuccess = true)
    }

    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse =
        transactionWrapper({
            val res = topicTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.searchString.isNotBlank()) {
                        add(
                            (topicTable.title like "%${rq.searchString}%")
                                    or (topicTable.description like "%${rq.searchString}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbTopicsResponse(data = res.map { topicTable.from(it, answerTable) }, isSuccess = true)
        }, {
            DbTopicsResponse.error(it.asMssError())
        })
}
