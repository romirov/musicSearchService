package ru.mss.blackbox.test.action

import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.mss.blackbox.fixture.client.Client


suspend fun Client.createMss(): Unit =
    withClue("createMss") {
        val response = sendAndReceive(
            "mss/create", """
                {
                    "name": "Bolt"
                }
            """.trimIndent()
        )

        response should haveNoErrors
    }
