package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.repo.stubs.TopicRepoStub
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationDeleteTest {

    private val command = MssCommand.DELETE
    private val settings by lazy {
        MssCorSettings(
            repoTest = TopicRepoStub()
        )
    }
    private val processor by lazy { MssTopicProcessor(settings) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)


}

