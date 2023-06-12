package com.crypto.cor

import com.crypto.cor.handlers.CorChainDslImpl
import com.crypto.cor.handlers.CorWorkerDslImpl
import com.crypto.cor.handlers.executeParallel

/**
 * Базовый билдер (dsl)
 */
@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): CorExec<T>
}

/**
 * Билдер (dsl) для цепочек (chain)
 */
@CorDslMarker
interface CorChainDsl<T> : ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

/**
 * Билдер (dsl) для рабочих (worker)
 */
@CorDslMarker
interface CorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}

/**
 * Точка входа в dsl построения цепочек.
 * Элементы исполняются последовательно.
 *
 * Пример:
 * ```
 *  chain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *      parallel {
 *         ...
 *      }
 *  }
 * ```
 */
fun <T> rootChain(function: CorChainDsl<T>.() -> Unit): CorChainDsl<T> = CorChainDslImpl<T>().apply(function)


/**
 * Создает цепочку, элементы которой исполняются последовательно.
 */
fun <T> CorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDslImpl<T>().apply(function))
}

/**
 * Создает цепочку, элементы которой исполняются параллельно. Будьте аккуратны с доступом к контексту -
 * при необходимости используйте синхронизацию
 */
fun <T> CorChainDsl<T>.parallel(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDslImpl<T>(::executeParallel).apply(function))
}

/**
 * Создает рабочего
 */
fun <T> CorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDslImpl<T>().apply(function))
}

/**
 * Создает рабочего с on и except по умолчанию
 */
fun <T> CorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDslImpl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}