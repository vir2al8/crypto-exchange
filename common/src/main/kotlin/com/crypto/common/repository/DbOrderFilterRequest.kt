package com.crypto.common.repository

import com.crypto.common.models.*

data class DbOrderFilterRequest(
    var type: CommonOrderType = CommonOrderType.NONE,
    var operation: CommonOrderOperation = CommonOrderOperation.NONE,
    var status: CommonOrderStatus = CommonOrderStatus.NONE,
)
