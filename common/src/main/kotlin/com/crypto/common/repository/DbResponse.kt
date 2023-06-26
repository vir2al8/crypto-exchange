package com.crypto.common.repository

import com.crypto.common.models.CommonError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<CommonError>
}