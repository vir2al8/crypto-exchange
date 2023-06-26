package com.crypto.common.permissions

import com.crypto.common.models.CommonWalletId

data class CommonPrincipalModel(
    val id: CommonWalletId = CommonWalletId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<CommonUserGroups> = emptySet()
) {
    companion object {
        val NONE = CommonPrincipalModel()
    }
}
