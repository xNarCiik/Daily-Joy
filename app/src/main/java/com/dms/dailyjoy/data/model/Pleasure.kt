package com.dms.dailyjoy.data.model

data class Pleasure(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val type: PleasureType = PleasureType.SMALL,
    val category: PleasureCategory? = null,
    val isFlipped: Boolean = false,
    val isDone: Boolean = false,
    val isCustom: Boolean = false
)