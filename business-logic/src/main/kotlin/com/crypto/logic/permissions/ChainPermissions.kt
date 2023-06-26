package com.crypto.logic.permissions

import com.crypto.authorization.resolveChainPermissions
import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker


fun CorChainDsl<CommonContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == CommonState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}
