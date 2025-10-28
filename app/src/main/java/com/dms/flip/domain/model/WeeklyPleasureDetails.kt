package com.dms.flip.domain.model

import com.dms.flip.data.model.Pleasure

data class WeeklyPleasureDetails(
    val pleasure: Pleasure,
    val dayOfWeek: Int,
    val completed: Boolean
)
