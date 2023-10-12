package ru.mss.app.ktor

import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext

private val processor = MssTopicProcessor()
suspend fun process(ctx: MssContext) = processor.exec(ctx)