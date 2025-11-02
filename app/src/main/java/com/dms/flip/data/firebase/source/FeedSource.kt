package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.CommentDto
import com.dms.flip.data.firebase.dto.PostDto
import com.dms.flip.domain.model.community.Paged
import kotlinx.coroutines.flow.Flow

interface FeedSource {
    data class PostDocument(
        val id: String,
        val data: PostDto
    )

    fun observeFriendsFeed(
        uid: String,
        limit: Int,
        cursor: String? = null
    ): Flow<Paged<PostDocument>>

    suspend fun toggleLike(postId: String, uid: String, like: Boolean)

    suspend fun addComment(postId: String, comment: CommentDto): Pair<String, CommentDto>

    suspend fun getComments(postId: String, limit: Int = 50): List<Pair<String, CommentDto>>

    suspend fun isPostLiked(postId: String, uid: String): Boolean
}
