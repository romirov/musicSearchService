package ru.mss.repo.tests

import ru.mss.common.repo.*

class TopicRepositoryMock(
    private val invokeCreateTopic: (DbTopicRequest) -> DbTopicResponse = { DbTopicResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadTopic: (DbTopicIdRequest) -> DbTopicResponse = { DbTopicResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateTopic: (DbTopicRequest) -> DbTopicResponse = { DbTopicResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteTopic: (DbTopicIdRequest) -> DbTopicResponse = { DbTopicResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchTopic: (DbTopicFilterRequest) -> DbTopicsResponse = { DbTopicsResponse.MOCK_SUCCESS_EMPTY },
): ITopicRepository {
    override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse {
        return invokeCreateTopic(rq)
    }

    override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse {
        return invokeReadTopic(rq)
    }

    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse {
        return invokeUpdateTopic(rq)
    }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse {
        return invokeDeleteTopic(rq)
    }

    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
        return invokeSearchTopic(rq)
    }
}
