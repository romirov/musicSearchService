package ru.mss.common.helpers

import ru.mss.common.MssContext
import ru.mss.common.exceptions.RepoConcurrencyException
import ru.mss.common.models.MssError
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicLock

fun Throwable.asMssError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MssError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun MssContext.addError(vararg error: MssError) = errors.addAll(error)

fun MssContext.fail(error: MssError) {
    addError(error)
    state = MssState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: MssError.Level = MssError.Level.ERROR,
) = MssError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: MssError.Level = MssError.Level.ERROR,
) = MssError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)

fun errorRepoConcurrency(
    expectedLock: MssTopicLock,
    actualLock: MssTopicLock?,
    exception: Exception? = null,
) = MssError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val errorNotFound = MssError(
    field = "id",
    message = "Not Found",
    code = "not-found"
)

val errorEmptyId = MssError(
    field = "id",
    message = "Id must not be null or blank"
)