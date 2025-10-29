package com.dms.flip.data.database.mapper

import com.dms.flip.data.database.entity.PleasureEntity
import com.dms.flip.data.model.Pleasure

fun PleasureEntity.toDomain() = Pleasure(
    id = id,
    title = title,
    description = description,
    category = category,
    isEnabled = isEnabled
)

fun Pleasure.toEntity() = PleasureEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    isEnabled = isEnabled
)

fun Pleasure.toFirestoreEntity(): Map<String, Any> = mapOf(
    "id" to id,
    "title" to title,
    "description" to description,
    "category" to category.name,
    "isEnabled" to isEnabled
)
