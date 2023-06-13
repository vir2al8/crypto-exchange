package com.crypto.logic.validation

import com.crypto.common.models.CommonCommand
import com.crypto.logic.OrderProcessor
import kotlin.test.Test

class ValidationDeleteTest {

    private val command = CommonCommand.DELETE
    private val processor by lazy { OrderProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}

