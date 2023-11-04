package ru.mss.repo.postgresql.test

import com.benasher44.uuid.uuid4
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
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        initObjects: Collection<MssTopic> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoTopicSQL {
        return RepoTopicSQL(
            SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
//                table = "topic-$test",
            ),
            initObjects,
            randomUuid = randomUuid
        )
    }
}
