package ru.mss.biz.permissions

import ru.mss.auth.checkPermitted
import ru.mss.auth.resolveRelationsTo
import ru.mss.common.MssContext
import ru.mss.common.helpers.fail
import ru.mss.common.models.MssError
import ru.mss.common.models.MssState
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.chain
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == MssState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        topicRepoRead.principalRelations = topicRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = checkPermitted(command, topicRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(MssError(message = "User is not allowed to perform this operation"))
        }
    }
}
