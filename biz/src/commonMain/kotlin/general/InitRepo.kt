package ru.mss.biz.general

import ru.mss.common.MssContext
import ru.mss.common.helpers.errorAdministration
import ru.mss.common.helpers.fail
import ru.mss.common.models.MssWorkMode
import ru.mss.common.repo.ITopicRepository
import ru.mss.lib.ICorChainDsl
import ru.mss.lib.worker

fun ICorChainDsl<MssContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        topicRepo = when {
            workMode == MssWorkMode.TEST -> settings.repoTest
            workMode == MssWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != MssWorkMode.STUB && topicRepo == ITopicRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
