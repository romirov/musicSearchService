package ru.mss.app.ktor.configs

import io.ktor.server.config.*

data class PostgresConfig(
//    val url: String = "jdbc:postgresql://localhost:5432/musicsearchservice",
    val url: String = "jdbc:postgresql://postgresql:5432/musicsearchservice",
    val user: String = "postgres",
    val password: String = "musicsearchservice-pass",
    val schema: String = "musicsearchservice",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.psql"
    }
}
