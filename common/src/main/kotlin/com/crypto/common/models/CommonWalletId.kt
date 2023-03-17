package com.crypto.common.models

@JvmInline
value class CommonWalletId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CommonWalletId("")
    }
}