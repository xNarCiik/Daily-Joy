package com.dms.flip.data.firebase.dto

data class RecentActivityDto(
    val pleasureTitle: String = "",
    val category: String = "",
    val completedAt: Long = 0L,
    val isCompleted: Boolean = false
)
