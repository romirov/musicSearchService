package ru.mss.common.exceptions

import ru.mss.common.models.MssTopicLock

class RepoConcurrencyException(expectedLock: MssTopicLock, actualLock: MssTopicLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
