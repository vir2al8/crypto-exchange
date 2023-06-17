package com.crypto.common.helpers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState

fun Throwable.asCommonError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CommonError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun CommonContext.addError(vararg error: CommonError) = errors.addAll(error)

fun CommonContext.fail(error: CommonError) {
    addError(error)
    state = CommonState.FAILING
}
fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
) = CommonError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
) = CommonError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    exception = exception,
)

val commonErrorNotFound = CommonError(
    field = "id",
    message = "Not Found",
    code = "not-found"
)

val commonErrorEmptyId = CommonError(
    field = "id",
    message = "Id must not be null or blank"
)
