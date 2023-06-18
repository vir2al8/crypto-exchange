package com.crypto.common

import com.crypto.common.repository.OrderRepository

data class CommonSettings(
    var repositoryStub: OrderRepository = OrderRepository.NONE,
    var repositoryTest: OrderRepository = OrderRepository.NONE,
    var repositoryProd: OrderRepository = OrderRepository.NONE
) {
    companion object {
        val NONE = CommonSettings()
    }
}