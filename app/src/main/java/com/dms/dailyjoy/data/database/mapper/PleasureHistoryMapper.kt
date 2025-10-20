package com.dms.dailyjoy.data.database.mapper

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntity
import com.dms.dailyjoy.data.model.Pleasure
import java.time.LocalDate

fun PleasureHistoryEntity.toPleasure(): Pleasure = Pleasure(
    id = this.pleasureId,
    title = this.title,
    description = this.description,
    type = this.type,
    category = this.category,
    date = this.date,
    isFlipped = this.isFlipped,
    isDone = this.isDone,
)

fun Pleasure.toHistoryEntity(): PleasureHistoryEntity = PleasureHistoryEntity(
    date = this.date ?: LocalDate.now(),
    pleasureId = this.id,
    title = this.title,
    description = this.description,
    type = this.type,
    category = this.category,
    isFlipped = this.isFlipped,
    isDone = this.isDone
)
