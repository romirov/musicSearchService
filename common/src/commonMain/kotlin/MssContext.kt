package ru.mss.common

import kotlinx.datetime.Instant
import ru.mss.common.models.*
import ru.mss.common.stubs.MssStubs

data class MssContext(
    var command: MssCommand = MssCommand.NONE,
    var state: MssState = MssState.NONE,
    val errors: MutableList<MssError> = mutableListOf(),
    var settings: MssCorSettings = MssCorSettings.NONE,

    var workMode: MssWorkMode = MssWorkMode.PROD,
    var stubCase: MssStubs = MssStubs.NONE,

    var adRepo: ITopicRepository = ITopicRepository.NONE,
    var adRepoRead: MssTopic = MssTopic(), // То, что прочитали из репозитория
    var adRepoPrepare: MssTopic = MssTopic(), // То, что готовим для сохранения в БД
    var adRepoDone: MssTopic = MssTopic(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<MssTopic> = mutableListOf(),

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
