package ru.mss.biz.permissions

import ru.mss.auth.resolveFrontPermissions
import ru.mss.auth.resolveRelationsTo
import ru.mss.common.MssContext
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == MssState.RUNNING }

    handle {
        topicRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                topicRepoDone.resolveRelationsTo(principal)
            )
        )

        for (topic in topicsRepoDone) {
            topic.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    topic.resolveRelationsTo(principal)
                )
            )
        }
    }
}
