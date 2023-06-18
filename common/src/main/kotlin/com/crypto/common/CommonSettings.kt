package com.crypto.common

import com.crypto.common.repository.OrderRepository

data class CommonSettings(
    var repoStub: OrderRepository = OrderRepository.NONE,
    var repoTest: OrderRepository = OrderRepository.NONE,
    var repoProd: OrderRepository = OrderRepository.NONE
) {
    companion object {
        val NONE = CommonSettings()
    }
}