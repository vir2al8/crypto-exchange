package com.crypto.logic.permissions

import com.crypto.authorization.checkPermitted
import com.crypto.authorization.resolveRelationsTo
import com.crypto.common.CommonContext
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.chain
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == CommonState.RUNNING }
    worker("Вычисление отношения order to principal") {
        orderRepositoryRead.principalRelations = orderRepositoryRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к orders") {
        permitted = checkPermitted(command, orderRepositoryRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(CommonError(message = "User is not allowed to perform this operation"))
        }
    }
}

