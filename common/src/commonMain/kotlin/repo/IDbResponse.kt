package ru.mss.common.repo

import ru.mss.common.models.MssError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<MssError>
}
