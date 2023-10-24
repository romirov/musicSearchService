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
fun validationDescriptionCorrect(command: MssCommand, processor: MssTopicProcessor) = runTest {
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
    assertEquals("abc", ctx.topicValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionTrim(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MssState.FAILING, ctx.state)
    assertEquals("abc", ctx.topicValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionEmpty(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = "abc",
            description = "",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionSymbols(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
