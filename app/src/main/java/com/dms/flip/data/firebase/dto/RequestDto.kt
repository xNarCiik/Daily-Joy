package com.dms.flip.data.firebase.dto

data class RequestDto(
    val userId: String = "",
    val username: String = "",
    val handle: String = "",
    val avatarUrl: String? = null,
    val requestedAt: Long = 0L
)
