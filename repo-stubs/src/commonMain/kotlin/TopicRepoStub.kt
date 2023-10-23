package ru.mss.repo.stubs

import ru.mss.common.models.MssTopicStatus
import ru.mss.common.repo.*
import ru.mss.stubs.MssTopicStub

class TopicRepoStub : ITopicRepository {
    override suspend fun createTopic(rq: DbTopicRequest): DbTopicResponse {
        return DbTopicResponse(
            data = MssTopicStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readTopic(rq: DbTopicIdRequest): DbTopicResponse {
        return DbTopicResponse(
            data = MssTopicStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateTopic(rq: DbTopicRequest): DbTopicResponse {
        return DbTopicResponse(
            data = MssTopicStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteTopic(rq: DbTopicIdRequest): DbTopicResponse {
        return DbTopicResponse(
            data = MssTopicStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchTopic(rq: DbTopicFilterRequest): DbTopicsResponse {
        return DbTopicsResponse(
            data = MssTopicStub.prepareSearchList(filter = "", MssTopicStatus.OPENED),
            isSuccess = true,
        )
    }
}
