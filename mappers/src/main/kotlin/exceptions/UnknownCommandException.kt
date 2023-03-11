package exceptions

import models.CommonCommand

class UnknownCommandException(command: CommonCommand) : RuntimeException("Unknown command $command")

