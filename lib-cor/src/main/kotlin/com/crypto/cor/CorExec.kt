package com.crypto.cor

interface CorExec<T> {
    val title: String
    val description: String

    suspend fun exec(context: T)
}