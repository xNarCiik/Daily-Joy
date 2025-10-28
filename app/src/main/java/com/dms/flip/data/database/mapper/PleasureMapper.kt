package com.dms.flip.data.database.mapper

import com.dms.flip.data.database.entity.PleasureEntity
import com.dms.flip.data.model.Pleasure

fun PleasureEntity.toDomain() = Pleasure(
    id = id,
    title = title,
    description = description,
    type = type,
    category = category,
    isCustom = isCustom,
    isEnabled = isEnabled
)

fun Pleasure.toEntity() = PleasureEntity(
    id = id,
    title = title,
    description = description,
    type = type,
    category = category,
    isCustom = isCustom,
    isEnabled = isEnabled
)
