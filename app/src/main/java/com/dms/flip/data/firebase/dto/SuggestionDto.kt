package com.dms.flip.data.firebase.dto

data class SuggestionDto(
    val username: String = "",
    val handle: String = "",
    val avatarUrl: String? = null,
    val mutualFriendsCount: Int = 0
)
