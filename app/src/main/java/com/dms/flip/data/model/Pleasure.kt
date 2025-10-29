package com.dms.flip.data.model

data class Pleasure(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: PleasureCategory = PleasureCategory.OTHER,
    val isEnabled: Boolean = true
)
