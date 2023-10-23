package ru.mss.common.repo

import ru.mss.common.models.MssError
import ru.mss.common.models.MssTopic

data class DbTopicsResponse(
    override val data: List<MssTopic>?,
    override val isSuccess: Boolean,
    override val errors: List<MssError> = emptyList(),
): IDbResponse<List<MssTopic>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbTopicsResponse(emptyList(), true)
        fun success(result: List<MssTopic>) = DbTopicsResponse(result, true)
        fun error(errors: List<MssError>) = DbTopicsResponse(null, false, errors)
        fun error(error: MssError) = DbTopicsResponse(null, false, listOf(error))
    }
}
