package com.crypto.cryptoexchange.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength

class KotestTest : FunSpec({

    test("my first test") {
        1 + 2 shouldBe 3
    }

    listOf(
        "sam",
        "pam",
        "tim",
        "123"
    ).forEach {
        test("$it should be a three letter name") {
            it.shouldHaveLength(3)
        }
    }

})
