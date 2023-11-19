package ru.mss.common

import kotlinx.datetime.Instant
import ru.mss.common.models.*
import ru.mss.common.permissions.MssPrincipalModel
import ru.mss.common.permissions.MssUserPermissions
import ru.mss.common.repo.ITopicRepository
import ru.mss.common.stubs.MssStubs

data class MssContext(
    var command: MssCommand = MssCommand.NONE,
    var state: MssState = MssState.NONE,
    val errors: MutableList<MssError> = mutableListOf(),
    var settings: MssCorSettings = MssCorSettings.NONE,

    var workMode: MssWorkMode = MssWorkMode.PROD,
    var stubCase: MssStubs = MssStubs.NONE,

    var topicRepo: ITopicRepository = ITopicRepository.NONE,
    var topicRepoRead: MssTopic = MssTopic(), // То, что прочитали из репозитория
    var topicRepoPrepare: MssTopic = MssTopic(), // То, что готовим для сохранения в БД
    var topicRepoDone: MssTopic = MssTopic(),  // Результат, полученный из БД
    var topicsRepoDone: MutableList<MssTopic> = mutableListOf(),

    var principal: MssPrincipalModel = MssPrincipalModel.NONE,
    val permissionsChain: MutableSet<MssUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: MssRequestId = MssRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var topicRequest: MssTopic = MssTopic(),
    var topicFilterRequest: MssTopicFilter = MssTopicFilter(),

    var topicValidating: MssTopic = MssTopic(),
    var topicFilterValidating: MssTopicFilter = MssTopicFilter(),

    var topicValidated: MssTopic = MssTopic(),
    var topicFilterValidated: MssTopicFilter = MssTopicFilter(),

    var topicResponse: MssTopic = MssTopic(),
    var topicsResponse: MutableList<MssTopic> = mutableListOf(),
)
