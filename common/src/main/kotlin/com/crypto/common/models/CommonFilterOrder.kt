package com.crypto.common.models

data class CommonFilterOrder(
    var type: CommonOrderType = CommonOrderType.NONE,
    var operation: CommonOrderOperation = CommonOrderOperation.NONE,
    var status: CommonOrderStatus = CommonOrderStatus.NONE,
)