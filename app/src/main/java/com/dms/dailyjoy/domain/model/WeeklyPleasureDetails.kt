package com.dms.dailyjoy.domain.model

import com.dms.dailyjoy.data.model.Pleasure

data class WeeklyPleasureDetails(
    val pleasure: Pleasure,
    val dayOfWeek: Int,
    val completed: Boolean
)
