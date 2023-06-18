package com.crypto.loggingcommon

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CommonLoggerProvider(
    private val provider: (String) -> CommonLogWrapper = { CommonLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}