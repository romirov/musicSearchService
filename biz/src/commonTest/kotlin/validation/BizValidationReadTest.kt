package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.models.MssCommand
import validation.validationIdCorrect
import validation.validationIdEmpty
import validation.validationIdFormat
import validation.validationIdTrim
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = MssCommand.READ
    private val processor by lazy { MssTopicProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}

