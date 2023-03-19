package com.crypto.mappers.exceptions

import com.crypto.common.models.CommonCommand

class UnknownCommandException(command: CommonCommand) : RuntimeException("Unknown command $command")

