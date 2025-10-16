package com.dms.dailyjoy.data.model

data class Pleasure(
    val id: Int,
    val title: String,
    val description: String,
    val type: PleasureType,
    val category: PleasureCategory,
    val isFlipped: Boolean = false,
    val isCustom: Boolean = false
)