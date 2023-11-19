package ru.mss.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.mss.biz.MssTopicProcessor
import ru.mss.biz.addTestPrincipal
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.stubs.MssTopicStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MssTopicStub.prepareResult { id = MssTopicId("123-234-abc-ABC") }

fun validationLockCorrect(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MssState.FAILING, ctx.state)
}

fun validationLockTrim(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MssState.FAILING, ctx.state)
}

fun validationLockEmpty(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: MssCommand, processor: MssTopicProcessor) = runTest {
    val ctx = MssContext(
        command = command,
        state = MssState.NONE,
        workMode = MssWorkMode.TEST,
        topicRequest = MssTopic(
            id = MssTopicId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            status = MssTopicStatus.OPENED,
            lock = MssTopicLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MssState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
