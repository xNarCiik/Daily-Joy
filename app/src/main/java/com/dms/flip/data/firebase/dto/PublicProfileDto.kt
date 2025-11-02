package com.dms.flip.data.firebase.dto

data class PublicProfileDto(
    val username: String = "",
    val handle: String = "",
    val avatarUrl: String? = null,
    val bio: String? = null,
    val stats: Map<String, Int> = emptyMap()
)
