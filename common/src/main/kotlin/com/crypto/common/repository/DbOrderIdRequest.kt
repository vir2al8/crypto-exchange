package com.crypto.common.repository

import com.crypto.common.models.CommonOrder
import com.crypto.common.models.CommonOrderId

data class DbOrderIdRequest(
    val id: CommonOrderId
) {
    constructor(order: CommonOrder) : this(order.id)
}
