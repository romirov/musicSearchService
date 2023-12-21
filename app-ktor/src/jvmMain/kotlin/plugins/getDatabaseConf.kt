package ru.mss.app.ktor.plugins

import io.ktor.server.application.*
import ru.mss.app.ktor.configs.ConfigPaths
import ru.mss.app.ktor.configs.PostgresConfig
import ru.mss.common.repo.ITopicRepository
import ru.mss.repo.inmemory.TopicRepoInMemory
import ru.mss.repo.postgresql.RepoTopicSQL
import ru.mss.repo.postgresql.SqlProperties
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class TopicDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: TopicDbType): ITopicRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initPostgres(): ITopicRepository {
    val config = PostgresConfig(environment.config)
    return RepoTopicSQL(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initInMemory(): ITopicRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return TopicRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
