package ru.mss.repo.postgresql

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.mss.common.models.*

class AnswerTable(answerTableName: String = "answer", topicTableName: String) : Table(answerTableName) {
    val id = varchar("id", 128)
    val topicId = reference("topic_id", TopicTable(topicTableName).id, onDelete = ReferenceOption.CASCADE)
    val user = varchar("user", 128)
    val body = text("body")


    fun from(res: ResultRow) = MssTopicAnswer(
        id = MssTopicAnswerId(res[id].toString()),
        userId = MssUserId(res[user].toString()),
        answerBody = body.toString()
    )

    fun to(it: UpdateBuilder<*>, topicId: String,  answer: MssTopicAnswer, randomUuid: () -> String) {
        it[id] = answer.id.takeIf { it != MssTopicAnswerId.NONE }?.asString() ?: randomUuid()
        it[this.topicId] = topicId
        it[user] = answer.userId.asString()
        it[body] = answer.answerBody
    }
}