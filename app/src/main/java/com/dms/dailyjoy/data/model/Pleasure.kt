package com.dms.dailyjoy.data.model

data class Pleasure(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val type: PleasureType = PleasureType.SMALL,
    val category: PleasureCategory = PleasureCategory.OTHER,
    val isCustom: Boolean = false,
    val isEnabled: Boolean = true
)
