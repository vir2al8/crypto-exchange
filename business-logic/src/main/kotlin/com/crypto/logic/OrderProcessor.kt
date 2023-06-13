package com.crypto.logic

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonOrderId
import com.crypto.common.models.CommonWalletId
import com.crypto.cor.rootChain
import com.crypto.cor.worker
import com.crypto.logic.groups.operation
import com.crypto.logic.validation.*
import com.crypto.logic.workers.initStatus
import com.crypto.logic.workers.stubs
import com.crypto.logic.workers.stubs.*

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
                validation {
                    worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                    worker("Очистка id") { orderValidating.id = CommonOrderId.NONE }
                    worker("Очистка wallet id") {
                        orderValidating.walletId = CommonWalletId(orderValidating.walletId.asString().trim())
                    }
                    validateWalletIdNotEmpty("Проверка на непустой wallet id")
                    validateWalletIdProperFormat("Проверка формата wallet id")
                    validateTypeNotNone("Проверка на недефолтный type")
                    validateOperationNotNone("Проверка на недефолтный operation")

                    finishOrderValidation("Завершение проверок")
                }
            }
            operation("Получить ордер", CommonCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                    worker("Очистка id") { orderValidating.id = CommonOrderId(orderValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishOrderValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Удалить ордер", CommonCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                    worker("Очистка id") { orderValidating.id = CommonOrderId(orderValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishOrderValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Поиск ордеров", CommonCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadOperation("Имитация ошибки валидации operation")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в orderFilterValidating") {
                        orderFilterValidating = orderFilterRequest.copy()
                    }

                    finishOrderFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}