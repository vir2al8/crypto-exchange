package com.crypto.common.repository

import com.crypto.common.helpers.commonErrorEmptyId
import com.crypto.common.helpers.commonErrorNotFound
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonOrder

data class DbOrdersResponse(
    override val data: List<CommonOrder>?,
    override val isSuccess: Boolean,
    override val errors: List<CommonError> = emptyList()
): DbResponse<List<CommonOrder>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbOrdersResponse(emptyList(), true)
        fun success(result: List<CommonOrder>) = DbOrdersResponse(result, true)
        fun error(errors: List<CommonError>) = DbOrdersResponse(null, false, errors)
        fun error(error: CommonError) = DbOrdersResponse(null, false, listOf(error))

        val errorEmptyId = error(commonErrorEmptyId)
        val errorNotFound = error(commonErrorNotFound)
    }
}