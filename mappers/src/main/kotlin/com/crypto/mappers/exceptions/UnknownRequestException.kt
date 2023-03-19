package com.crypto.mappers.exceptions

import kotlin.reflect.KClass

class UnknownRequestException(kClass: KClass<*>) : RuntimeException("Class $kClass cannot be mapped and not supported")
