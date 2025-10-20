package com.dms.dailyjoy.data.database.mapper

import com.dms.dailyjoy.data.database.entity.PleasureEntity
import com.dms.dailyjoy.data.model.Pleasure

fun Pleasure.toEntity(): PleasureEntity {
    return PleasureEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        type = this.type,
        category = this.category,
        isFlipped = this.isFlipped,
        isDone = this.isDone,
        isCustom = this.isCustom
    )
}

fun PleasureEntity.toDomain(): Pleasure {
    return Pleasure(
        id = this.id,
        title = this.title,
        description = this.description,
        type = this.type,
        category = this.category,
        isFlipped = this.isFlipped,
        isDone = this.isDone,
        isCustom = this.isCustom
    )
}
