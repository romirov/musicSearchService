package ru.mss.app.ktor.repo


import com.benasher44.uuid.uuid4
import kotlinx.coroutines.runBlocking
import org.testcontainers.containers.PostgreSQLContainer
import ru.mss.common.models.MssTopic
import ru.mss.repo.postgresql.RepoTopicSQL
import ru.mss.repo.postgresql.SqlProperties
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "musicsearchservice-pass"
    private const val SCHEMA = "musicsearchservice"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
        }
    }

    fun start() {
        container.start()
    }

    fun stop() {
        container.stop()
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        initObjects: Collection<MssTopic> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoTopicSQL = RepoTopicSQL(
        properties = SqlProperties(
            url = url,
            user = USER,
            password = PASS,
            schema = SCHEMA,
            tableTopic = "topic-$test",
            tableAnswer = "answer-$test",
        ),
        initObjects = initObjects,
        randomUuid = randomUuid
    )
}
