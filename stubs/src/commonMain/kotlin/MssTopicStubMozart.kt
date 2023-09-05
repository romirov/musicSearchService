package ru.mss.stubs

import ru.mss.common.models.*

object MssTopicStubMozart {
    val TOPIC_OPEN_MOZART: MssTopic
        get() = MssTopic(
            id = MssTopicId("666"),
            title = "Неизвестная композиция",
            description = "Неизвестна композиция неизвестного автора",
            ownerId = MssUserId("user-1"),
            status = MssTopicStatus.OPENED,
            answers = mutableListOf(
                MssTopicAnswer(
                    id = MssTopicAnswerId("1"),
                    userId = MssUserId("user-11"),
                    answerBody = "предположительно Моцарт"
                )
            ),
            permissionsClient = mutableSetOf(
                MssTopicPermissionClient.READ,
                MssTopicPermissionClient.UPDATE,
                MssTopicPermissionClient.DELETE,
                MssTopicPermissionClient.MAKE_OPENED,
                MssTopicPermissionClient.MAKE_CLOSED,
            )
        )
    val TOPIC_CLOSE_MOZART = TOPIC_OPEN_MOZART.copy(status = MssTopicStatus.CLOSED)
}