package ru.mss.repo.gremlin.mappers

import ru.mss.common.models.MssTopic


fun MssTopic.label(): String? = this::class.simpleName
