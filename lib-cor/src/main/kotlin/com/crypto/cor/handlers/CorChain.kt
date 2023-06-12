package com.crypto.cor.handlers

import com.crypto.cor.CorDslMarker
import com.crypto.cor.CorChainDsl
import com.crypto.cor.CorExec
import com.crypto.cor.ICorExecDsl
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CorChain<T>(
    title: String,
    description: String = "",
    private val execs: List<CorExec<T>>,
    private val handler: suspend (T, List<CorExec<T>>) -> Unit,
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) = handler(context, execs)
}

suspend fun <T> executeSequential(context: T, execs: List<CorExec<T>>): Unit =
    execs.forEach {
        it.exec(context)
    }

suspend fun <T> executeParallel(context: T, execs: List<CorExec<T>>): Unit = coroutineScope {
    execs.forEach {
        launch { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDslImpl<T>(
    private val handler: suspend (T, List<CorExec<T>>) -> Unit = ::executeSequential,
) : CorExecDsl<T>(), CorChainDsl<T> {
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): CorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        handler = handler,
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}