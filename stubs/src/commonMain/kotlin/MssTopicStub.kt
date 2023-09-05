package ru.mss.stubs

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicStatus
import ru.mss.stubs.MssTopicStubMozart.TOPIC_CLOSE_MOZART
import ru.mss.stubs.MssTopicStubMozart.TOPIC_OPEN_MOZART

object MssTopicStub {
    fun get(): MssTopic = TOPIC_OPEN_MOZART.copy()

    fun prepareResult(block: MssTopic.() -> Unit): MssTopic = get().apply(block)

    fun prepareSearchList(filter: String, status: MssTopicStatus) = listOf(
        mssTopicOpen("d-666-01", filter, status),
        mssTopicOpen("d-666-02", filter, status),
        mssTopicOpen("d-666-03", filter, status),
        mssTopicOpen("d-666-04", filter, status),
        mssTopicOpen("d-666-05", filter, status),
        mssTopicOpen("d-666-06", filter, status),
    )

    private fun mssTopicOpen(id: String, filter: String, status: MssTopicStatus) =
        mssTopic(TOPIC_OPEN_MOZART, id = id, filter = filter, status = status)

    private fun mssTopicClose(id: String, filter: String, status: MssTopicStatus) =
        mssTopic(TOPIC_CLOSE_MOZART, id = id, filter = filter, status = status)

    private fun mssTopic(base: MssTopic, id: String, filter: String, status: MssTopicStatus) = base.copy(
        id = MssTopicId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        status = status,
    )
}