package com.crypto.logic.validation

import com.crypto.common.CommonSettings
import com.crypto.common.models.CommonCommand
import com.crypto.logic.OrderProcessor
import com.crypto.repostub.OrderRepositoryStub
import kotlin.test.Test

class ValidationCreateTest {
    private val settings by lazy {
        CommonSettings(
            repositoryTest = OrderRepositoryStub()
        )
    }

    private val command = CommonCommand.CREATE
    private val processor by lazy { OrderProcessor(settings) }

    @Test fun correctWalletId() = validationWalletIdCorrect(command, processor)
    @Test fun trimWalletId() = validationWalletIdTrim(command, processor)
    @Test fun emptyWalletId() = validationWalletIdEmpty(command, processor)
    @Test fun badFormatWalletId() = validationWalletIdFormat(command, processor)

    @Test fun correctType() = validationTypeCorrect(command, processor)
    @Test fun noneType() = validationTypeNone(command, processor)

    @Test fun correctOperation() = validationOperationCorrect(command, processor)
    @Test fun noneOperation() = validationOperationNone(command, processor)

}

