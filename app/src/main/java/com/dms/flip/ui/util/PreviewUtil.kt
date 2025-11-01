package com.dms.flip.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.ui.community.CommunityTab
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.Friend
import com.dms.flip.ui.community.FriendPleasure
import com.dms.flip.ui.community.FriendPost
import com.dms.flip.ui.community.FriendRequest
import com.dms.flip.ui.community.FriendRequestSource
import com.dms.flip.ui.community.FriendSuggestion
import com.dms.flip.ui.community.PleasureStatus
import com.dms.flip.ui.community.PublicProfile
import com.dms.flip.ui.community.RecentActivity
import com.dms.flip.ui.community.RelationshipStatus
import com.dms.flip.ui.community.SuggestionSource
import com.dms.flip.ui.community.UserSearchResult
import com.dms.flip.ui.history.WeeklyDay

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = "0",
    title = "Pleasure Title",
    description = "Pleasure Description",
    category = PleasureCategory.CREATIVE
)

val previewFriends = listOf(
    Friend(
        id = "1",
        username = "Damien",
        currentPleasure = FriendPleasure(
            title = "Fumé un pet.. ou deux",
            status = PleasureStatus.COMPLETED,
            category = PleasureCategory.WELLNESS
        ),
        avatarUrl = null,
        handle = "@damien.lgx",
        streak = 12,
        isOnline = true,
        favoriteCategory = PleasureCategory.WELLNESS
    ),
    Friend(
        id = "2",
        username = "La mère michek",
        handle = "@emmamiaa",
        streak = 24,
        currentPleasure = FriendPleasure(
            title = "Baiser Ugo",
            status = PleasureStatus.IN_PROGRESS,
            category = PleasureCategory.FOOD
        ),
        avatarUrl = null,
        isOnline = false,
        favoriteCategory = PleasureCategory.SPORT
    ),
    Friend(
        id = "3",
        username = "Chloé Lefèvre",
        handle = "@chloe.lefevre",
        avatarUrl = null,
        streak = 25,
        isOnline = true,
        currentPleasure = FriendPleasure(
            title = "Randonnée en forêt",
            category = PleasureCategory.OUTDOOR,
            status = PleasureStatus.COMPLETED
        ),
        favoriteCategory = PleasureCategory.OUTDOOR
    ),
    Friend(
        id = "4",
        username = "Lucas Dubois",
        handle = "@lucas.dubois",
        avatarUrl = null,
        streak = 5,
        isOnline = false,
        currentPleasure = FriendPleasure(
            title = "Préparer un brunch",
            category = PleasureCategory.FOOD,
            status = PleasureStatus.IN_PROGRESS
        ),
        favoriteCategory = PleasureCategory.FOOD
    ),
    Friend(
        id = "5",
        username = "Sophie Martin",
        handle = "@sophie.martin",
        avatarUrl = null,
        streak = 15,
        isOnline = true,
        currentPleasure = FriendPleasure(
            title = "Lecture inspirante",
            category = PleasureCategory.CULTURE,
            status = PleasureStatus.IN_PROGRESS
        ),
        favoriteCategory = PleasureCategory.CULTURE
    )
)

val previewPosts = listOf(
    FriendPost(
        id = "p1",
        friend = previewFriends[0],
        content = "Magnifique session de méditation ce matin ! Je me sens tellement apaisé 🧘‍♀️✨",
        timestamp = System.currentTimeMillis() - 300000, // 5min ago
        likesCount = 12,
        commentsCount = 3,
        isLiked = false,
        pleasureCategory = PleasureCategory.WELLNESS,
        pleasureTitle = "Méditation guidée",
        comments = emptyList()
    ),
    FriendPost(
        id = "p2",
        friend = previewFriends[1],
        content = "Course matinale sous le soleil, parfait pour commencer la journée ! 🏃‍♂️☀️",
        timestamp = System.currentTimeMillis() - 1920000, // 32min ago
        likesCount = 28,
        commentsCount = 2,
        isLiked = true,
        pleasureCategory = PleasureCategory.SPORT,
        pleasureTitle = "Run de quartier",
        comments = emptyList()
    ),
    FriendPost(
        id = "p3",
        friend = previewFriends[2],
        content = "La nature est tellement ressourçante. Cette randonnée était exactement ce dont j'avais besoin 🌲💚",
        timestamp = System.currentTimeMillis() - 3600000, // 1h ago
        likesCount = 45,
        commentsCount = 4,
        isLiked = false,
        pleasureCategory = PleasureCategory.OUTDOOR,
        pleasureTitle = "Balade en forêt",
        comments = emptyList()
    ),
    FriendPost(
        id = "p4",
        friend = previewFriends[3],
        content = "Préparation d'un délicieux petit-déjeuner healthy 🥑🍳 La journée commence bien !",
        timestamp = System.currentTimeMillis() - 7200000, // 2h ago
        likesCount = 34,
        commentsCount = 5,
        isLiked = true,
        pleasureCategory = PleasureCategory.FOOD,
        pleasureTitle = "Brunch du dimanche",
        comments = emptyList()
    ),
    FriendPost(
        id = "p5",
        friend = previewFriends[4],
        content = "Lecture de mon nouveau livre préféré avec un bon café ☕📖 Moment parfait",
        timestamp = System.currentTimeMillis() - 10800000, // 3h ago
        likesCount = 21,
        commentsCount = 1,
        isLiked = false,
        pleasureCategory = PleasureCategory.CULTURE,
        pleasureTitle = "Pause lecture",
        comments = emptyList()
    )
)

val previewSuggestions = listOf(
    FriendSuggestion(
        id = "s1",
        username = "Alexandre Dupont",
        handle = "@alex.dupont",
        avatarUrl = null,
        mutualFriendsCount = 5,
        source = SuggestionSource.ALGORITHM
    ),
    FriendSuggestion(
        id = "s2",
        username = "Marie Claire",
        handle = "@marie_claire",
        avatarUrl = null,
        mutualFriendsCount = 2,
        source = SuggestionSource.COMMUNITY
    ),
    FriendSuggestion(
        id = "s3",
        username = "Lucas Martin",
        handle = "@lucasm",
        avatarUrl = null,
        mutualFriendsCount = 8,
        source = SuggestionSource.CONTACTS
    ),
    FriendSuggestion(
        id = "s4",
        username = "Emma Rousseau",
        handle = "@emma.r",
        avatarUrl = null,
        mutualFriendsCount = 3,
        source = SuggestionSource.ALGORITHM
    )
)

val previewPendingRequests = listOf(
    FriendRequest(
        id = "r1",
        userId = "u1",
        username = "Alexandre Moreau",
        handle = "@alex.moreau",
        avatarUrl = null,
        requestedAt = System.currentTimeMillis() - 172800000, // 2 days ago
        source = FriendRequestSource.SEARCH
    ),
    FriendRequest(
        id = "r2",
        userId = "u2",
        username = "Julie Petit",
        handle = "@julie.petit",
        avatarUrl = null,
        requestedAt = System.currentTimeMillis() - 432000000, // 5 days ago
        source = FriendRequestSource.SUGGESTION
    ),
    FriendRequest(
        id = "r3",
        userId = "u3",
        username = "Thomas Bernard",
        handle = "@thomas.b",
        avatarUrl = null,
        requestedAt = System.currentTimeMillis() - 86400000, // 1 day ago
        source = FriendRequestSource.SEARCH
    )
)

val previewSentRequests = listOf(
    FriendRequest(
        id = "r4",
        userId = "u4",
        username = "Théo Martin",
        handle = "@theom",
        avatarUrl = null,
        requestedAt = System.currentTimeMillis() - 86400000, // 1 day ago
        source = FriendRequestSource.SEARCH
    ),
    FriendRequest(
        id = "r5",
        userId = "u5",
        username = "Léa Dubois",
        handle = "@lea.db",
        avatarUrl = null,
        requestedAt = System.currentTimeMillis() - 259200000, // 3 days ago
        source = FriendRequestSource.SUGGESTION
    )
)

val previewSearchResults = listOf(
    UserSearchResult(
        id = "sr1",
        username = "Alice Martin",
        handle = "@alice.martin",
        avatarUrl = null,
        relationshipStatus = RelationshipStatus.NONE
    ),
    UserSearchResult(
        id = "sr2",
        username = "Alice Durand",
        handle = "@aliced",
        avatarUrl = null,
        relationshipStatus = RelationshipStatus.PENDING_SENT
    ),
    UserSearchResult(
        id = "sr3",
        username = "Ali Celo",
        handle = "@alicelo",
        avatarUrl = null,
        relationshipStatus = RelationshipStatus.FRIEND
    ),
    UserSearchResult(
        id = "sr4",
        username = "Alicia Roberts",
        handle = "@alicia.r",
        avatarUrl = null,
        relationshipStatus = RelationshipStatus.PENDING_RECEIVED
    )
)

val previewPublicProfile = PublicProfile(
    id = "profile1",
    username = "Amélie Dubois",
    handle = "@amelie.db",
    avatarUrl = null,
    bio = "Chercher le soleil au quotidien et partager des moments de joie 🌞✨",
    friendsCount = 128,
    daysCompleted = 312,
    currentStreak = 42,
    recentActivities = listOf(
        RecentActivity(
            id = "a1",
            pleasureTitle = "Méditation matinale",
            category = PleasureCategory.WELLNESS,
            completedAt = System.currentTimeMillis() - 86400000, // Yesterday
            isCompleted = true
        ),
        RecentActivity(
            id = "a2",
            pleasureTitle = "Journal de gratitude",
            category = PleasureCategory.WELLNESS,
            completedAt = System.currentTimeMillis() - 172800000, // 2 days ago
            isCompleted = true
        ),
        RecentActivity(
            id = "a3",
            pleasureTitle = "Marche en pleine nature",
            category = PleasureCategory.OUTDOOR,
            completedAt = System.currentTimeMillis() - 259200000, // 3 days ago
            isCompleted = true
        ),
        RecentActivity(
            id = "a4",
            pleasureTitle = "Lecture inspirante",
            category = PleasureCategory.CULTURE,
            completedAt = System.currentTimeMillis() - 345600000, // 4 days ago
            isCompleted = true
        ),
        RecentActivity(
            id = "a5",
            pleasureTitle = "Café en terrasse",
            category = PleasureCategory.SOCIAL,
            completedAt = System.currentTimeMillis() - 432000000, // 5 days ago
            isCompleted = true
        )
    ),
    relationshipStatus = RelationshipStatus.NONE
)

/**
 * État de preview avec tous les onglets remplis
 */
val previewCommunityUiStateFull = CommunityUiState(
    isLoading = false,
    selectedTab = CommunityTab.FRIENDS,
    friendsPosts = previewPosts,
    friends = previewFriends,
    suggestions = previewSuggestions,
    pendingRequests = previewPendingRequests,
    sentRequests = previewSentRequests,
    searchQuery = "",
    searchResults = emptyList(),
    error = null
)

/**
 * État de preview avec recherche active
 */
val previewCommunityUiStateSearching = CommunityUiState(
    isLoading = false,
    selectedTab = CommunityTab.FRIENDS,
    friendsPosts = previewPosts,
    friends = previewFriends,
    suggestions = previewSuggestions,
    searchQuery = "Alice",
    isSearching = false,
    searchResults = previewSearchResults
)


val previewWeeklyDays = listOf(
    WeeklyDay(
        dayName = "Lundi",
        historyEntry = PleasureHistory(
            id = "1",
            dateDrawn = System.currentTimeMillis() - 86400000 * 2,
            completed = true,
            pleasureTitle = "Savourer un café chaud",
            pleasureDescription = "Prendre le temps de déguster",
            pleasureCategory = PleasureCategory.FOOD
        ),
        dateMillis = 0
    ),
    WeeklyDay(
        dayName = "Mardi",
        historyEntry = PleasureHistory(
            id = "2",
            dateDrawn = System.currentTimeMillis() - 86400000,
            completed = true,
            pleasureTitle = "Lire quelques pages d'un livre",
            pleasureDescription = "Se plonger dans une histoire",
            pleasureCategory = PleasureCategory.LEARNING
        ),
        dateMillis = 0
    ),
    WeeklyDay(
        dayName = "Mercredi",
        historyEntry = PleasureHistory(
            id = "3",
            dateDrawn = System.currentTimeMillis(),
            completed = false,
            pleasureTitle = "Plaisir du jour",
            pleasureDescription = "",
            pleasureCategory = PleasureCategory.ALL
        ),
        dateMillis = 0
    ),
    WeeklyDay(dayName = "Jeudi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Vendredi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Samedi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Dimanche", historyEntry = null, dateMillis = 0)
)
