package ru.mss.blackbox.test

import io.kotest.core.annotation.Ignored
import ru.mss.blackbox.docker.WiremockDockerCompose
import ru.mss.blackbox.fixture.BaseFunSpec
import ru.mss.blackbox.fixture.client.RestClient
import ru.mss.blackbox.fixture.docker.DockerCompose

@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val client = RestClient(dockerCompose)

    testApiV1(client)
})
class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)
// TODO class AccRestSpringTest : AccRestTestBase(SpringDockerCompose)
// TODO class AccRestKtorTest : AccRestTestBase(KtorDockerCompose)
