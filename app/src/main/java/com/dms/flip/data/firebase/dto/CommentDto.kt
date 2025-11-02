package com.dms.flip.data.firebase.dto

data class CommentDto(
    val userId: String = "",
    val username: String = "",
    val userHandle: String = "",
    val avatarUrl: String? = null,
    val content: String = "",
    val timestamp: Long = 0L
)
