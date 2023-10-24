package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.stubs.MssTopicStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MssTopicStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = "abc",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MssState.FAILING, ctx.state)
    assertEquals("abc", ctx.topicValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MssState.FAILING, ctx.state)
    assertEquals("abc", ctx.topicValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = "",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("123"),
            title = "!@#$%^&*(),.{}",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
