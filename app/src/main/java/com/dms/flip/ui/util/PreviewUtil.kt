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
        username = "Dams",
        currentPleasure = FriendPleasure(
            title = "Roule un pet (ou 2...)",
            status = PleasureStatus.COMPLETED,
            category = PleasureCategory.ALL
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/456069928_491069416973778_8102649957987195103_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=106&_nc_oc=Q6cZ2QG4rVoBJ6sEYElGW-AZn5ifCGVV4Qg-RnrUnVQSXmgzZJ189fPNePshBzEbq9hzsUwZ3_S0Oj78n-tW1lxsk1El&_nc_ohc=69HpQ1dXh1oQ7kNvwGew0jG&_nc_gid=qQvGEAaks1sFiF5Pgln3TA&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_Afe5-ZY9gDxZDwHDRhtevkGtBWHwxZb0UOl1sjrroCMmqg&oe=6905BB85&_nc_sid=7a9f4b",
        handle = "@dams.lgx",
        streak = 12,
        isOnline = true,
        favoriteCategory = PleasureCategory.WELLNESS
    ),
    Friend(
        id = "2",
        username = "Emmamamiaa",
        handle = "@lameremichel",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Ugo",
            status = PleasureStatus.IN_PROGRESS,
            category = PleasureCategory.FOOD
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/401916050_1385369609057390_7192697213845317191_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=102&_nc_oc=Q6cZ2QG4mR4RIAnPWJkdfmmNTU9oeuRlKgEE3kTQU4lF92yTXp8jSKMijP1KZ55MRlykKk4nEuVru-gHA16dRm1c-sXO&_nc_ohc=RFejnkIOLcQQ7kNvwF9_ktT&_nc_gid=LYJo9EvRL8Air7a2-OiXpw&edm=APoiHPcBAAAA&ccb=7-5&oh=00_AfeVT-3g_LHDNgjFYrchKVMdASIzoWca0h9-ip0aAUPMnQ&oe=6905CF01&_nc_sid=22de04",
        isOnline = false,
        favoriteCategory = PleasureCategory.SPORT
    ),
    Friend(
        id = "3",
        username = "Chloé Lefevre",
        handle = "@chloe.lefev",
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
        handle = "@lucas.db",
        avatarUrl = null,
        streak = 5,
        isOnline = false,
        currentPleasure = null,
        favoriteCategory = PleasureCategory.FOOD
    ),
    Friend(
        id = "5",
        username = "Sophie Martin",
        handle = "@sophie.m",
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
        content = "Magnifique session de méditation ce matin ! Je me sens tellement apaisée 🧘‍♀️✨",
        timestamp = System.currentTimeMillis() - 300000, // 5min ago
        likesCount = 12,
        commentsCount = 3,
        isLiked = false,
        pleasureCategory = PleasureCategory.WELLNESS
    ),
    FriendPost(
        id = "p2",
        friend = previewFriends[1],
        content = "Course matinale sous le soleil, parfait pour commencer la journée ! 🏃‍♂️☀️",
        timestamp = System.currentTimeMillis() - 1920000, // 32min ago
        likesCount = 28,
        commentsCount = 7,
        isLiked = true,
        pleasureCategory = PleasureCategory.SPORT
    ),
    FriendPost(
        id = "p3",
        friend = previewFriends[2],
        content = "La nature est tellement ressourçante. Cette randonnée était exactement ce dont j'avais besoin 🌲💚",
        timestamp = System.currentTimeMillis() - 3600000, // 1h ago
        likesCount = 45,
        commentsCount = 12,
        isLiked = false,
        pleasureCategory = PleasureCategory.OUTDOOR
    ),
    FriendPost(
        id = "p4",
        friend = previewFriends[3],
        content = "Préparation d'un délicieux petit-déjeuner healthy 🥑🍳 La journée commence bien !",
        timestamp = System.currentTimeMillis() - 7200000, // 2h ago
        likesCount = 34,
        commentsCount = 8,
        isLiked = true,
        pleasureCategory = PleasureCategory.FOOD
    ),
    FriendPost(
        id = "p5",
        friend = previewFriends[4],
        content = "Lecture de mon nouveau livre préféré avec un bon café ☕📖 Moment parfait",
        timestamp = System.currentTimeMillis() - 10800000, // 3h ago
        likesCount = 21,
        commentsCount = 5,
        isLiked = false,
        pleasureCategory = PleasureCategory.CULTURE
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
 * État de preview avec onglet Amis vide
 */
val previewCommunityUiStateEmptyFriends = CommunityUiState(
    isLoading = false,
    selectedTab = CommunityTab.FRIENDS,
    friendsPosts = emptyList(),
    friends = emptyList(),
    suggestions = previewSuggestions,
    pendingRequests = previewPendingRequests,
    sentRequests = emptyList()
)

/**
 * État de preview avec onglet Suggestions vide
 */
val previewCommunityUiStateEmptySuggestions = CommunityUiState(
    isLoading = false,
    selectedTab = CommunityTab.SUGGESTIONS,
    friendsPosts = previewPosts,
    friends = previewFriends,
    suggestions = emptyList(),
    pendingRequests = previewPendingRequests,
    sentRequests = previewSentRequests
)

/**
 * État de preview avec onglet Invitations vide
 */
val previewCommunityUiStateEmptyInvitations = CommunityUiState(
    isLoading = false,
    selectedTab = CommunityTab.INVITATIONS,
    friendsPosts = previewPosts,
    friends = previewFriends,
    suggestions = previewSuggestions,
    pendingRequests = emptyList(),
    sentRequests = emptyList()
)

/**
 * État de preview en chargement
 */
val previewCommunityUiStateLoading = CommunityUiState(
    isLoading = true,
    selectedTab = CommunityTab.FRIENDS,
    friendsPosts = emptyList(),
    friends = emptyList(),
    suggestions = emptyList(),
    pendingRequests = emptyList(),
    sentRequests = emptyList()
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

/*listOf(
Friend(
    id = "1",
    username = "Dams",
    streak = 8,
    currentPleasure = FriendPleasure(
        title = "Roule un pet (ou 2...)",
        status = PleasureStatus.COMPLETED,
        category = PleasureCategory.ALL
    ),
    avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/456069928_491069416973778_8102649957987195103_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=106&_nc_oc=Q6cZ2QG4rVoBJ6sEYElGW-AZn5ifCGVV4Qg-RnrUnVQSXmgzZJ189fPNePshBzEbq9hzsUwZ3_S0Oj78n-tW1lxsk1El&_nc_ohc=69HpQ1dXh1oQ7kNvwGew0jG&_nc_gid=qQvGEAaks1sFiF5Pgln3TA&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_Afe5-ZY9gDxZDwHDRhtevkGtBWHwxZb0UOl1sjrroCMmqg&oe=6905BB85&_nc_sid=7a9f4b"
),
Friend(
    id = "2",
    username = "Emma",
    streak = 326,
    currentPleasure = FriendPleasure(
        title = "Baiser Ugo",
        status = PleasureStatus.IN_PROGRESS,
        category = PleasureCategory.FOOD
    ),
    avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/401916050_1385369609057390_7192697213845317191_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=102&_nc_oc=Q6cZ2QG4mR4RIAnPWJkdfmmNTU9oeuRlKgEE3kTQU4lF92yTXp8jSKMijP1KZ55MRlykKk4nEuVru-gHA16dRm1c-sXO&_nc_ohc=RFejnkIOLcQQ7kNvwF9_ktT&_nc_gid=LYJo9EvRL8Air7a2-OiXpw&edm=APoiHPcBAAAA&ccb=7-5&oh=00_AfeVT-3g_LHDNgjFYrchKVMdASIzoWca0h9-ip0aAUPMnQ&oe=6905CF01&_nc_sid=22de04"
),
Friend(
    id = "3",
    username = "Alisson",
    streak = 2,
    avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/525189438_18151745419389304_842152024012543561_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=104&_nc_oc=Q6cZ2QFRY4LwbkbPH1o2W_p0VzGJt1r0vqzYerN_8TsB4PXSNpx5USn-CoIxZ1gM7IioBtJ-XR43LminTMMTURd8ztCY&_nc_ohc=QIvDbT-G2_AQ7kNvwHp6Fza&_nc_gid=fd7JWnTKgdG63EvhrVlNCg&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_AfdOjvelQI3znS_i6a2z-T1CdP3VPhs26YuZQegoLjO3RA&oe=6905BBC5&_nc_sid=7a9f4b"
),
Friend(
    id = "4",
    username = "Antho",
    streak = 2,
    currentPleasure = FriendPleasure(
        title = "GROSSSE TEUFFFF",
        status = PleasureStatus.IN_PROGRESS,
        category = PleasureCategory.ENTERTAINMENT
    ),
    avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/502718768_18065806517284093_1519982854029510988_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmV4cGVyaW1lbnRhbCJ9&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=101&_nc_oc=Q6cZ2QG4VLM_L5sBKFBia4cPbNgPJ43treZK8vAKwBM3Ut-cNBHfA9zMAbVUGCNlherSvAM74r7RdvQXRkyeHZ7_QdqX&_nc_ohc=pfY-A-GA6qsQ7kNvwHfacbe&_nc_gid=e7tUP4MQXedgTCgMy1xN1w&edm=ALGbJPMBAAAA&ccb=7-5&oh=00_Afd_jgFnDoADkxlT2qWkVcELEhbascTUVwnFKTtY0ODp2Q&oe=6905B1C1&_nc_sid=7d3ac5"
),
Friend(
    id = "5",
    username = "Lilou la fripouille",
    streak = 33,
    currentPleasure = FriendPleasure(
        title = "Appeler mon petit frère et lui tapper la causette h24",
        status = PleasureStatus.COMPLETED,
        category = PleasureCategory.SOCIAL
    ),
    avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/543112697_18301432420247093_348652955831382183_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby41NDkuYzIifQ&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=105&_nc_oc=Q6cZ2QEE6lZarhdFjXx3xHmMEZsR_baiOqSHKIXHktWDZ8P1janqlnFy_0NacBb3jnxCZEWaHe3rYc7oZSO1qLnpexd0&_nc_ohc=uWd8IyzNH58Q7kNvwFXQHjy&_nc_gid=-5FDa0QEFTWLM6xUhBpTvw&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_Afcq1SdfVr6AaoiNk0G24x-VIF-CTrQyWDDmy6XKY2ARVw&oe=6905CD5F&_nc_sid=7a9f4b"
)
) */

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
