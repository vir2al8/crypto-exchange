package com.crypto.logic.permissions

import com.crypto.authorization.resolveFrontPermissions
import com.crypto.authorization.resolveRelationsTo
import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == CommonState.RUNNING }

    handle {
        orderRepositoryDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                orderRepositoryDone.resolveRelationsTo(principal)
            )
        )

        for (order in ordersRepositoryDone) {
            order.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    order.resolveRelationsTo(principal)
                )
            )
        }
    }
}
