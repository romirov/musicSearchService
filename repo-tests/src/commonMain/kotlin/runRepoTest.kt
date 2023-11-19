package ru.otus.otuskotlin.marketplace.backend.repo.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}