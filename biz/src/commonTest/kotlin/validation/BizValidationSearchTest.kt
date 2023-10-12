package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicFilter
import ru.mss.common.models.MssWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = MssCommand.SEARCH
    private val processor by lazy { MssTopicProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = MssContext(
            command = command,
            state = MssState.NONE,
            workMode = MssWorkMode.TEST,
            topicFilterRequest = MssTopicFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MssState.FAILING, ctx.state)
    }
}

