package com.crypto.logic

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonOrderId
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWalletId
import com.crypto.cor.chain
import com.crypto.cor.rootChain
import com.crypto.cor.worker
import com.crypto.logic.groups.operation
import com.crypto.logic.permissions.accessValidation
import com.crypto.logic.permissions.chainPermissions
import com.crypto.logic.permissions.frontPermissions
import com.crypto.logic.repository.*
import com.crypto.logic.validation.*
import com.crypto.logic.workers.initRepository
import com.crypto.logic.workers.initStatus
import com.crypto.logic.workers.prepareResult
import com.crypto.logic.workers.stubs
import com.crypto.logic.workers.stubs.*

class OrderProcessor(
    private val settings: CommonSettings = CommonSettings()
) {
    suspend fun exec(ctx: CommonContext) = BusinessChain.exec(ctx.apply { settings = this@OrderProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<CommonContext> {
            initStatus("Инициализация статуса")
            initRepository("Инициализация репозитория")

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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repositoryPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Вычисление прав доступа")
                    repositoryCreate("Создание order in db")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repositoryRead("Read order from db")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == CommonState.RUNNING }
                        handle { orderRepositoryDone = orderRepositoryRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repositoryRead("Read order from db")
                    accessValidation("Вычисление прав доступа")
                    repositoryPrepareDelete("Подготовка объекта для удаления")
                    repositoryDelete("Delete order from db")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                repositorySearch("Search orders in db by filter")
                accessValidation("Вычисление прав доступа")
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}