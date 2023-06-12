package com.crypto.logic

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonCommand
import com.crypto.cor.rootChain
import com.crypto.logic.groups.operation
import com.crypto.logic.workers.initStatus
import com.crypto.logic.workers.stubs
import ru.otus.otuskotlin.marketplace.biz.workers.*

class OrderProcessor {
    suspend fun exec(ctx: CommonContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<CommonContext> {
            initStatus("Инициализация статуса")

            operation("Создание ордера", CommonCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadType("Имитация ошибки валидации type")
                    stubValidationBadWalletId("Имитация ошибки валидации wallet id")
                    stubValidationBadOperation("Имитация ошибки валидации operation")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить ордер", CommonCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Удалить ордер", CommonCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Поиск ордеров", CommonCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadOperation("Имитация ошибки валидации operation")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
        }.build()
    }
}