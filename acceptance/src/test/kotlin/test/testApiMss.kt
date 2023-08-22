package ru.mss.blackbox.test

import io.kotest.core.spec.style.FunSpec
import ru.mss.blackbox.fixture.client.Client
import ru.mss.blackbox.test.action.createMss

fun FunSpec.testApiV1(client: Client) {
    context("v1") {
        test("Create Mss ok") {
            client.createMss()
        }
    }
}