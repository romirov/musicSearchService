package ru.mss.auth

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.permissions.MssPrincipalModel
import ru.mss.common.permissions.MssPrincipalRelations

fun MssTopic.resolveRelationsTo(principal: MssPrincipalModel): Set<MssPrincipalRelations> = setOfNotNull(
    MssPrincipalRelations.NONE,
    MssPrincipalRelations.NEW.takeIf { id == MssTopicId.NONE },
    MssPrincipalRelations.OWN.takeIf { principal.id == ownerId },
)
