package com.crypto.configs

import com.crypto.common.models.CommonWalletId
import com.crypto.common.permissions.CommonPrincipalModel
import com.crypto.common.permissions.CommonUserGroups
import com.crypto.configs.KtorAuthConfig.Companion.F_NAME_CLAIM
import com.crypto.configs.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.crypto.configs.KtorAuthConfig.Companion.ID_CLAIM
import com.crypto.configs.KtorAuthConfig.Companion.L_NAME_CLAIM
import com.crypto.configs.KtorAuthConfig.Companion.M_NAME_CLAIM
import io.ktor.server.auth.jwt.*

fun JWTPrincipal?.toModel() = this?.run {
    CommonPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { CommonWalletId(it) } ?: CommonWalletId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> CommonUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: CommonPrincipalModel.NONE