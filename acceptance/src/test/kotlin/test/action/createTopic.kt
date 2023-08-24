package ru.mss.blackbox.test.action

import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.mss.blackbox.fixture.client.Client


suspend fun Client.createTopic(): Unit =
    withClue("createTopic") {
        val response = sendAndReceive(
            "topic/create", """
                {
                    "name": "Artist name"
                }
            """.trimIndent()
        )

        response should haveNoErrors
    }
