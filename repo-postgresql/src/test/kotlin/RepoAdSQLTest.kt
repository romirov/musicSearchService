package ru.mss.repo.postgresql.test

import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.tests.RepoTopicCreateTest
import ru.mss.repo.tests.RepoTopicDeleteTest
import ru.mss.repo.tests.RepoTopicSearchTest
import ru.mss.repo.tests.RepoTopicUpdateTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.RepoTopicReadTest
import kotlin.random.Random

val random = Random(System.currentTimeMillis())
class RepoAdSQLCreateTest : RepoTopicCreateTest() {
    override val repo: ITopicRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSQLDeleteTest : RepoTopicDeleteTest() {
    override val repo: ITopicRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete_" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLReadTest : RepoTopicReadTest() {
    override val repo: ITopicRepository = SqlTestCompanion.repoUnderTestContainer(
        "read" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLSearchTest : RepoTopicSearchTest() {
    override val repo: ITopicRepository = SqlTestCompanion.repoUnderTestContainer(
        "search" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLUpdateTest : RepoTopicUpdateTest() {
    override val repo: ITopicRepository = SqlTestCompanion.repoUnderTestContainer(
        "update" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}