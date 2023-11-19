package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicFilter
import ru.mss.common.models.MssWorkMode
import ru.mss.repo.stubs.TopicRepoStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest {

    private val command = MssCommand.SEARCH
    private val settings by lazy {
        MssCorSettings(
            repoTest = TopicRepoStub()
        )
    }
    private val processor by lazy { MssTopicProcessor(settings) }

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

