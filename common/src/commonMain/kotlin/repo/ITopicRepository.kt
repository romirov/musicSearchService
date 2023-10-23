package ru.mss.common.repo

interface ITopicRepository {
    suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse
    suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse
    suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse
    suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse
    suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse
    companion object {
        val NONE = object : ITopicRepository {
            override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
