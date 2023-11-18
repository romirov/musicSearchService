package ru.mss.repo.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicLock
import ru.mss.common.models.MssUserId
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_AD_TYPE
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_DESCRIPTION
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_ID
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_LOCK
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_OWNER_ID
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_TITLE
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_TMP_RESULT
import ru.mss.repo.gremlin.TopicGremlinConst.FIELD_VISIBILITY
import ru.mss.repo.gremlin.TopicGremlinConst.RESULT_SUCCESS
import ru.mss.repo.gremlin.exceptions.WrongEnumException

fun GraphTraversal<Vertex, Vertex>.addMssTopic(topic: MssTopic): GraphTraversal<Vertex, Vertex> =
    this
        .property(VertexProperty.Cardinality.single, FIELD_TITLE, topic.title.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_DESCRIPTION, topic.description.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, topic.lock.takeIf { it != MssTopicLock.NONE }?.asString())
        .property(
            VertexProperty.Cardinality.single,
            FIELD_OWNER_ID,
            topic.ownerId.asString().takeIf { it.isNotBlank() }) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single, FIELD_AD_TYPE, topic.adType.takeIf { it != MssDealSide.NONE }?.name)
        .property(
            VertexProperty.Cardinality.single,
            FIELD_VISIBILITY,
            topic.visibility.takeIf { it != MssVisibility.NONE }?.name
        )

fun GraphTraversal<Vertex, Vertex>.listMssTopic(result: String = RESULT_SUCCESS): GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_OWNER_ID,
        FIELD_LOCK,
        FIELD_TITLE,
        FIELD_DESCRIPTION,
        FIELD_AD_TYPE,
        FIELD_VISIBILITY,
        FIELD_TMP_RESULT,
    )
        .by(gr.id<Vertex>())
        .by(FIELD_OWNER_ID)
//        .by(gr.inE("Owns").outV().id())
        .by(FIELD_LOCK)
        .by(FIELD_TITLE)
        .by(FIELD_DESCRIPTION)
        .by(FIELD_AD_TYPE)
        .by(FIELD_VISIBILITY)
        .by(gr.constant(result))
//        .by(elementMap<Vertex, Map<Any?, Any?>>())

fun Map<String, Any?>.toMssTopic(): MssTopic = MssTopic(
    id = (this[FIELD_ID] as? String)?.let { MssTopicId(it) } ?: MssTopicId.NONE,
    ownerId = (this[FIELD_OWNER_ID] as? String)?.let { MssUserId(it) } ?: MssUserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { MssTopicLock(it) } ?: MssTopicLock.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    description = (this[FIELD_DESCRIPTION] as? String) ?: "",
    adType = when (val value = this[FIELD_AD_TYPE] as? String) {
        MssDealSide.SUPPLY.name -> MssDealSide.SUPPLY
        MssDealSide.DEMAND.name -> MssDealSide.DEMAND
        null -> MssDealSide.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "adType = '$value' cannot be converted to ${MssDealSide::class}"
        )
    },
    visibility = when (val value = this[FIELD_VISIBILITY]) {
        MssVisibility.VISIBLE_PUBLIC.name -> MssVisibility.VISIBLE_PUBLIC
        MssVisibility.VISIBLE_TO_GROUP.name -> MssVisibility.VISIBLE_TO_GROUP
        MssVisibility.VISIBLE_TO_OWNER.name -> MssVisibility.VISIBLE_TO_OWNER
        null -> MssVisibility.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "visibility = '$value' cannot be converted to ${MssVisibility::class}"
        )
    },
)
