package ru.mss.biz

import ru.mss.common.MssContext
import ru.mss.common.models.MssUserId
import ru.mss.common.permissions.MssPrincipalModel
import ru.mss.common.permissions.MssUserGroups

fun MssContext.addTestPrincipal(userId: MssUserId = MssUserId("321")) {
    principal = MssPrincipalModel(
        id = userId,
        groups = setOf(
            MssUserGroups.USER,
            MssUserGroups.TEST,
        )
    )
}
