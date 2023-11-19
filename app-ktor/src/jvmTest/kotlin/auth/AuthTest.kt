package ru.mss.app.ktor.auth

import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.mss.app.common.AuthConfig
import ru.mss.app.ktor.helpers.testSettings
import ru.mss.app.ktor.module
import ru.mss.app.ktor.moduleJvm
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            module(testSettings())
        }

        val response = client.post("/v1/topic/create") {
            addAuth(config = AuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
