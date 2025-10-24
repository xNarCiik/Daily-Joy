package com.dms.dailyjoy.data.database.mapper

import com.dms.dailyjoy.data.database.entity.PleasureEntity
import com.dms.dailyjoy.data.model.Pleasure

fun PleasureEntity.toDomain() = Pleasure(
    id = id,
    title = title,
    description = description,
    type = type,
    category = category,
    isDone = isDone,
    isCustom = isCustom,
    isEnabled = isEnabled
)

fun Pleasure.toEntity() = PleasureEntity(
    id = id,
    title = title,
    description = description,
    type = type,
    category = category,
    isDone = isDone,
    isCustom = isCustom,
    isEnabled = isEnabled
)
