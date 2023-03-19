package com.crypto.common.models

@JvmInline
value class CommonRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CommonRequestId("")
    }
}