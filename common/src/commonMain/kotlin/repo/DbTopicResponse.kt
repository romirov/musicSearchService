package ru.mss.common.repo

import ru.mss.common.helpers.errorRepoConcurrency
import ru.mss.common.models.MssError
import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicLock
import ru.mss.common.helpers.errorEmptyId as mssErrorEmptyId
import ru.mss.common.helpers.errorNotFound as mssErrorNotFound

data class DbTopicResponse(
    override val data: MssTopic?,
    override val isSuccess: Boolean,
    override val errors: List<MssError> = emptyList()
): IDbResponse<MssTopic> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTopicResponse(null, true)
        fun success(result: MssTopic) = DbTopicResponse(result, true)
        fun error(errors: List<MssError>, data: MssTopic? = null) = DbTopicResponse(data, false, errors)
        fun error(error: MssError, data: MssTopic? = null) = DbTopicResponse(data, false, listOf(error))

        val errorEmptyId = error(mssErrorEmptyId)

        fun errorConcurrent(lock: MssTopicLock, topic: MssTopic?) = error(
            errorRepoConcurrency(lock, topic?.lock?.let { MssTopicLock(it.asString()) }),
            topic
        )

        val errorNotFound = error(mssErrorNotFound)
    }
}
