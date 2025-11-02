package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.FriendPost
import com.dms.flip.domain.model.community.Paged
import com.dms.flip.domain.model.community.PostComment
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun observeFriendsFeed(limit: Int, cursor: String? = null): Flow<Paged<FriendPost>>
    suspend fun toggleLike(postId: String, like: Boolean)
    suspend fun addComment(postId: String, content: String): PostComment
}
