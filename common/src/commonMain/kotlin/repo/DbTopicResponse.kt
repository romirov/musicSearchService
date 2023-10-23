package ru.mss.common.repo

import ru.mss.common.models.MssError
import ru.mss.common.models.MssTopic

data class DbTopicResponse(
    override val data: MssTopic?,
    override val isSuccess: Boolean,
    override val errors: List<MssError> = emptyList()
): IDbResponse<MssTopic> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTopicResponse(null, true)
        fun success(result: MssTopic) = DbTopicResponse(result, true)
        fun error(errors: List<MssError>) = DbTopicResponse(null, false, errors)
        fun error(error: MssError) = DbTopicResponse(null, false, listOf(error))
    }
}
