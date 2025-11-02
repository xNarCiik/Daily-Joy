package com.dms.flip.data.firebase.dto

data class PostDto(
    val authorId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val pleasureCategory: String? = null,
    val pleasureTitle: String? = null,
    val likes_count: Int = 0,
    val comments_count: Int = 0
)
