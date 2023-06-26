package com.crypto.common.repository

import com.crypto.common.helpers.commonErrorEmptyId
import com.crypto.common.helpers.commonErrorNotFound
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonOrder

data class DbOrderResponse(
    override val data: CommonOrder?,
    override val isSuccess: Boolean,
    override val errors: List<CommonError> = emptyList()
): DbResponse<CommonOrder> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbOrderResponse(null, true)
        fun success(result: CommonOrder) = DbOrderResponse(result, true)
        fun error(errors: List<CommonError>, data: CommonOrder? = null) = DbOrderResponse(data, false, errors)
        fun error(error: CommonError, data: CommonOrder? = null) = DbOrderResponse(data, false, listOf(error))

        val errorEmptyId = error(commonErrorEmptyId)
        val errorNotFound = error(commonErrorNotFound)
    }
}