package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.repo.stubs.TopicRepoStub
import kotlin.test.Test

// TODO-validation-5: смотрим пример теста валидации, собранного из тестовых функций-оберток
class BizValidationCreateTest {

    private val command = MssCommand.CREATE
    private val settings by lazy {
        MssCorSettings(
            repoTest = TopicRepoStub()
        )
    }
    private val processor by lazy { MssTopicProcessor(settings) }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

}

