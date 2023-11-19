package ru.mss.app.ktor.base

import io.ktor.server.auth.jwt.*
import ru.mss.app.common.AuthConfig.Companion.F_NAME_CLAIM
import ru.mss.app.common.AuthConfig.Companion.GROUPS_CLAIM
import ru.mss.app.common.AuthConfig.Companion.ID_CLAIM
import ru.mss.app.common.AuthConfig.Companion.L_NAME_CLAIM
import ru.mss.app.common.AuthConfig.Companion.M_NAME_CLAIM
import ru.mss.common.models.MssUserId
import ru.mss.common.permissions.MssPrincipalModel
import ru.mss.common.permissions.MssUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    MssPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { MssUserId(it) } ?: MssUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> MssUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MssPrincipalModel.NONE
