package ru.mss.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.mss.common.models.*

class TopicTable(tableName: String = "topic") : Table(tableName) {
    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = text("description")
    val owner = varchar("owner", 128)
    val status = enumeration("visibility", MssTopicStatus::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow, answerTable: AnswerTable) = MssTopic(
        id = MssTopicId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = MssUserId(res[owner].toString()),
        status = res[status],
        answers = answerTable
            .select { answerTable.topicId eq res[id] }
            .map { answerRes -> answerTable.from(answerRes) }.toMutableList(),
        lock = MssTopicLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, topic: MssTopic, randomUuid: () -> String) {
        val topicId = topic.id.takeIf { it != MssTopicId.NONE }?.asString() ?: randomUuid()
        it[id] = topicId
        it[title] = topic.title
        it[description] = topic.description
        it[owner] = topic.ownerId.asString()
        it[status] = topic.status
        it[lock] = topic.lock.takeIf { it != MssTopicLock.NONE }?.asString() ?: randomUuid()
    }
}
