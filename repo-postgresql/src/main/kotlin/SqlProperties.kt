package ru.mss.repo.postgresql

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/musicsearchservice",
    val user: String = "postgres",
    val password: String = "musicsearchservice-pass",
    val schema: String = "musicsearchservice",
    val tableTopic: String = "topic",
    val tableAnswer: String = "answer",
)
