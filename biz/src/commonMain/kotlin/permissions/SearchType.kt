package ru.mss.biz.permissions

import ru.mss.common.MssContext
import ru.mss.common.models.MssSearchPermissions
import ru.mss.common.models.MssState
import ru.mss.common.permissions.MssUserPermissions
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.chain
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == MssState.RUNNING }
    worker("Определение типа поиска") {
        topicFilterValidated.searchPermissions = setOfNotNull(
            MssSearchPermissions.OWN.takeIf { permissionsChain.contains(MssUserPermissions.SEARCH_OWN) },
            MssSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(MssUserPermissions.SEARCH_PUBLIC) },
            MssSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(MssUserPermissions.SEARCH_REGISTERED) },
        ).toMutableSet()
    }
}
