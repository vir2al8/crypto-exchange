package com.crypto.common

import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import java.time.Instant

data class CommonContext(
    var command: CommonCommand = CommonCommand.NONE,
    var state: CommonState = CommonState.NONE,
    var errors: MutableList<CommonError> = mutableListOf(),

    var workMode: CommonWorkMode = CommonWorkMode.PROD,
    var stubCase: CommonStub = CommonStub.NONE,

    var requestId: CommonRequestId = CommonRequestId.NONE,
    var timeStart: Instant = Instant.MIN,
    var orderRequest: CommonOrder = CommonOrder(),
    var orderFilterRequest: CommonOrderFilter = CommonOrderFilter(),

    var orderValidating: CommonOrder = CommonOrder(),
    var orderFilterValidating: CommonOrderFilter = CommonOrderFilter(),

    var orderValidated: CommonOrder = CommonOrder(),
    var orderFilterValidated: CommonOrderFilter = CommonOrderFilter(),

    var orderResponse: CommonOrder = CommonOrder(),
    var ordersResponse: MutableList<CommonOrder> = mutableListOf()
)