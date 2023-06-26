package com.crypto.cor.handlers

import com.crypto.cor.CorExec
import com.crypto.cor.CorExecDsl

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend  T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = {},
) : CorExec<T> {
    protected abstract suspend fun handle(context: T)

    private suspend fun on (context: T): Boolean = context.blockOn()

    private suspend fun except(context: T, exception: Throwable) = context.blockExcept(exception)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (exception : Throwable) {
                except(context, exception)
            }
        }
    }
}

abstract class CorExecDslImpl<T> : CorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}