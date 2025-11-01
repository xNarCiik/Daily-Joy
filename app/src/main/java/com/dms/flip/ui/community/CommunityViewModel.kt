package com.dms.flip.ui.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.R
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.util.previewFriends
import com.dms.flip.ui.util.previewPendingRequests
import com.dms.flip.ui.util.previewSentRequests
import com.dms.flip.ui.util.previewSuggestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    // TODO: Inject real use cases when ready
    /*
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val addFriendUseCase: AddFriendUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val declineFriendRequestUseCase: DeclineFriendRequestUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    */
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState = _uiState.asStateFlow()

    // Cache des profils publics pour la navigation
    private val _publicProfiles = mutableMapOf<String, PublicProfile>()

    init {
        loadMockData()
    }

    /**
     * Charge les données mock pour le développement
     * TODO: Remplacer par de vrais use cases
     */
    private fun loadMockData() {
        _uiState.update {
            it.copy(
                friends = previewFriends,
                friendsPosts = generateMockPosts(),
                suggestions = previewSuggestions,
                pendingRequests = previewPendingRequests,
                sentRequests = previewSentRequests
            )
        }
    }

    /**
     * Récupère un profil public par son ID
     * Utilisé pour la navigation vers les profils
     */
    fun getPublicProfile(userId: String): PublicProfile? {
        // Vérifier le cache
        if (_publicProfiles.containsKey(userId)) {
            return _publicProfiles[userId]
        }

        // Créer un profil depuis un ami si disponible
        val friend = _uiState.value.friends.find { it.id == userId }
        if (friend != null) {
            val profile = PublicProfile(
                id = friend.id,
                username = friend.username,
                handle = friend.handle,
                avatarUrl = friend.avatarUrl,
                bio = "Passionné de bien-être et de mindfulness 🧘‍♀️",
                friendsCount = (10..150).random(),
                daysCompleted = (50..500).random(),
                currentStreak = friend.streak,
                recentActivities = generateMockActivities(),
                relationshipStatus = RelationshipStatus.FRIEND
            )
            _publicProfiles[userId] = profile
            return profile
        }

        // Créer depuis les résultats de recherche
        val searchResult = _uiState.value.searchResults.find { it.id == userId }
        if (searchResult != null) {
            val profile = PublicProfile(
                id = searchResult.id,
                username = searchResult.username,
                handle = searchResult.handle,
                avatarUrl = searchResult.avatarUrl,
                bio = "Nouveau sur Flip ! En quête de moments de joie quotidiens ✨",
                friendsCount = (5..50).random(),
                daysCompleted = (10..100).random(),
                currentStreak = (0..30).random(),
                recentActivities = generateMockActivities(),
                relationshipStatus = searchResult.relationshipStatus
            )
            _publicProfiles[userId] = profile
            return profile
        }

        // Créer depuis une suggestion
        val suggestion = _uiState.value.suggestions.find { it.id == userId }
        if (suggestion != null) {
            val profile = PublicProfile(
                id = suggestion.id,
                username = suggestion.username,
                handle = suggestion.handle,
                avatarUrl = suggestion.avatarUrl,
                bio = "Bienvenue sur mon profil Flip ! 🌟",
                friendsCount = (15..80).random(),
                daysCompleted = (30..200).random(),
                currentStreak = (0..20).random(),
                recentActivities = generateMockActivities(),
                relationshipStatus = RelationshipStatus.NONE
            )
            _publicProfiles[userId] = profile
            return profile
        }

        // Créer depuis une demande reçue
        val pendingRequest = _uiState.value.pendingRequests.find { it.userId == userId }
        if (pendingRequest != null) {
            val profile = PublicProfile(
                id = pendingRequest.userId,
                username = pendingRequest.username,
                handle = pendingRequest.handle,
                avatarUrl = pendingRequest.avatarUrl,
                bio = "Nouveau sur Flip ! En quête de nouvelles connexions 🤝",
                friendsCount = (5..50).random(),
                daysCompleted = (10..100).random(),
                currentStreak = (0..30).random(),
                recentActivities = generateMockActivities(),
                relationshipStatus = RelationshipStatus.PENDING_RECEIVED
            )
            _publicProfiles[userId] = profile
            return profile
        }

        // Créer depuis une demande envoyée
        val sentRequest = _uiState.value.sentRequests.find { it.userId == userId }
        if (sentRequest != null) {
            val profile = PublicProfile(
                id = sentRequest.userId,
                username = sentRequest.username,
                handle = sentRequest.handle,
                avatarUrl = sentRequest.avatarUrl,
                bio = "Bienvenue sur Flip ! ✨",
                friendsCount = (10..60).random(),
                daysCompleted = (20..150).random(),
                currentStreak = (0..25).random(),
                recentActivities = generateMockActivities(),
                relationshipStatus = RelationshipStatus.PENDING_SENT
            )
            _publicProfiles[userId] = profile
            return profile
        }

        return null
    }

    /**
     * Génère des activités récentes mock pour les profils
     */
    private fun generateMockActivities(): List<RecentActivity> {
        val categories = PleasureCategory.entries
        val pleasureTitles = listOf(
            "Méditation matinale",
            "Balade en forêt",
            "Lecture inspirante",
            "Yoga doux",
            "Café en terrasse",
            "Journal de gratitude",
            "Marche en pleine nature",
            "Moment créatif",
            "Écoute musicale",
            "Pause thé"
        )

        return List(5) { index ->
            RecentActivity(
                id = "activity_$index",
                pleasureTitle = pleasureTitles.random(),
                category = categories.random(),
                completedAt = System.currentTimeMillis() - (index * 86400000L),
                isCompleted = index < 3
            )
        }
    }

    /**
     * Point d'entrée unique pour tous les événements utilisateur
     */
    fun onEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }

            is CommunityEvent.OnCreatePostClicked -> {
                // TODO: Navigation vers l'écran de création de post
            }

            // ==================== FRIENDS FEED ====================
            is CommunityEvent.OnPostLiked -> togglePostLike(event.postId)

            is CommunityEvent.OnToggleComments -> {
                _uiState.update {
                    it.copy(
                        expandedPostId = if (it.expandedPostId == event.postId) null else event.postId
                    )
                }
            }

            is CommunityEvent.OnAddComment -> {
                addComment(event.postId, event.comment)
            }

            is CommunityEvent.OnPostMenuClicked -> {
                // Menu géré dans l'UI avec BottomSheet
            }

            is CommunityEvent.OnFriendMenuClicked -> {
                // TODO: Show friend action menu
            }

            is CommunityEvent.OnInviteFriendToPleasure -> {
                inviteFriendToPleasure(event.friend)
            }

            is CommunityEvent.OnRemoveFriend -> removeFriend(event.friend)

            is CommunityEvent.OnAddSuggestion -> addSuggestion(event.suggestion)

            is CommunityEvent.OnHideSuggestion -> hideSuggestion(event.suggestion)

            // ==================== FRIEND REQUESTS ====================
            is CommunityEvent.OnAcceptFriendRequest -> acceptFriendRequest(event.request)

            is CommunityEvent.OnDeclineFriendRequest -> declineFriendRequest(event.request)

            is CommunityEvent.OnCancelSentRequest -> cancelSentRequest(event.request)

            // ==================== SEARCH ====================
            is CommunityEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                if (event.query.isNotBlank()) {
                    searchUsers(event.query)
                } else {
                    _uiState.update { it.copy(searchResults = emptyList()) }
                }
            }

            is CommunityEvent.OnSearchResultClicked -> {
                // Navigation gérée dans le NavHost
            }

            is CommunityEvent.OnAddUserFromSearch -> addUserFromSearch(event.userId)

            // ==================== ERROR HANDLING ====================
            is CommunityEvent.OnRetryClicked -> {
                _uiState.update { it.copy(error = null, isLoading = false) }
                loadMockData()
            }

            else -> { } // Navigation gérée dans le NavHost
        }
    }

    // ==================== PRIVATE METHODS ====================

    /**
     * Toggle le like d'un post
     */
    private fun togglePostLike(postId: String) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                friendsPosts = state.friendsPosts.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = !post.isLiked,
                            likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
                        )
                    } else post
                }
            )
        }
        // TODO: Call likePostUseCase
    }

    /**
     * Retire un ami de la liste
     */
    private fun removeFriend(friend: Friend) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                friends = state.friends.filter { it.id != friend.id },
                friendsPosts = state.friendsPosts.filter { it.friend.id != friend.id }
            )
        }
        // TODO: Call removeFriendUseCase
    }

    /**
     * Ajoute une suggestion comme ami (envoie une demande)
     */
    private fun addSuggestion(suggestion: FriendSuggestion) = viewModelScope.launch {
        // Créer une nouvelle demande envoyée
        val newRequest = FriendRequest(
            id = "temp_${System.currentTimeMillis()}",
            userId = suggestion.id,
            username = suggestion.username,
            handle = suggestion.handle,
            avatarUrl = suggestion.avatarUrl,
            requestedAt = System.currentTimeMillis(),
            source = FriendRequestSource.SUGGESTION
        )

        _uiState.update { state ->
            state.copy(
                sentRequests = state.sentRequests + newRequest,
                suggestions = state.suggestions.filter { it.id != suggestion.id }
            )
        }
        // TODO: Call sendFriendRequestUseCase
    }

    /**
     * Masque une suggestion
     */
    private fun hideSuggestion(suggestion: FriendSuggestion) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(suggestions = state.suggestions.filter { it.id != suggestion.id })
        }
        // TODO: Call hideSuggestionUseCase
    }

    /**
     * Accepte une demande d'ami
     */
    private fun acceptFriendRequest(request: FriendRequest) = viewModelScope.launch {
        // Créer un nouvel ami
        val newFriend = Friend(
            id = request.userId,
            username = request.username,
            handle = request.handle,
            avatarUrl = request.avatarUrl,
            streak = 0,
            isOnline = false,
            currentPleasure = null,
            favoriteCategory = null
        )

        _uiState.update { state ->
            state.copy(
                friends = state.friends + newFriend,
                pendingRequests = state.pendingRequests.filter { it.id != request.id }
            )
        }
        // TODO: Call acceptFriendRequestUseCase
    }

    /**
     * Refuse une demande d'ami
     */
    private fun declineFriendRequest(request: FriendRequest) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(pendingRequests = state.pendingRequests.filter { it.id != request.id })
        }
        // TODO: Call declineFriendRequestUseCase
    }

    /**
     * Annule une demande d'ami envoyée
     */
    private fun cancelSentRequest(request: FriendRequest) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(sentRequests = state.sentRequests.filter { it.id != request.id })
        }
        // TODO: Call cancelFriendRequestUseCase
    }

    /**
     * Recherche des utilisateurs
     */
    private fun searchUsers(query: String) = viewModelScope.launch {
        if (query.length < 2) return@launch

        _uiState.update { it.copy(isSearching = true) }

        // Simuler une recherche avec délai
        delay(300) // Debounce

        // Mock search results
        val mockResults = listOf(
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
            ),
            UserSearchResult(
                id = "sr5",
                username = "Alexandre Dubois",
                handle = "@alex.db",
                avatarUrl = null,
                relationshipStatus = RelationshipStatus.NONE
            )
        ).filter {
            it.username.contains(query, ignoreCase = true) ||
                    it.handle.contains(query, ignoreCase = true)
        }

        _uiState.update {
            it.copy(
                searchResults = mockResults,
                isSearching = false
            )
        }
        // TODO: Call searchUsersUseCase
    }

    /**
     * Ajoute un utilisateur depuis la recherche
     */
    private fun addUserFromSearch(userId: String) = viewModelScope.launch {
        // Créer une nouvelle demande envoyée
        val user = _uiState.value.searchResults.find { it.id == userId }
        if (user != null) {
            val newRequest = FriendRequest(
                id = "temp_${System.currentTimeMillis()}",
                userId = user.id,
                username = user.username,
                handle = user.handle,
                avatarUrl = user.avatarUrl,
                requestedAt = System.currentTimeMillis(),
                source = FriendRequestSource.SEARCH
            )

            _uiState.update { state ->
                state.copy(
                    sentRequests = state.sentRequests + newRequest,
                    searchResults = state.searchResults.map { result ->
                        if (result.id == userId) {
                            result.copy(relationshipStatus = RelationshipStatus.PENDING_SENT)
                        } else result
                    }
                )
            }
        }
        // TODO: Call sendFriendRequestUseCase
    }

    /**
     * Invite un ami à partager un plaisir
     */
    private fun inviteFriendToPleasure(friend: Friend) = viewModelScope.launch {
        // TODO: Navigate to pleasure selection screen with friendId
        // TODO: Call inviteFriendToPleasureUseCase
    }

    // ==================== PUBLIC UTILITY METHODS ====================

    /**
     * Rafraîchit toutes les données
     */
    fun refresh() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            // TODO: Call all use cases in parallel
            // val friends = getFriendsUseCase()
            // val posts = getFriendPostsUseCase()
            // val suggestions = getSuggestionsUseCase()
            // val requests = getFriendRequestsUseCase()

            loadMockData() // Temporaire

            _uiState.update { it.copy(isLoading = false) }
        } catch (_: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = R.string.error_load_friends
                )
            }
        }
    }


    private fun generateMockPosts(): List<FriendPost> {
        val categories = PleasureCategory.entries

        return previewFriends.take(5).mapIndexed { index, friend ->
            val category = categories.random()
            FriendPost(
                id = "post_$index",
                friend = friend,
                content = when (index) {
                    0 -> "Quelle belle matinée ! Séance de méditation terminée, je me sens incroyablement zen 🧘‍♀️✨"
                    1 -> "Pause sportive avec un footing léger, idéal pour relâcher la pression"
                    2 -> "Lecture du moment"
                    3 -> "Séance de yoga"
                    4 -> "Pause café"
                    else -> "Moment créatif du jour"
                },
                timestamp = System.currentTimeMillis() - (index * 3600000L),
                likesCount = (5..50).random(),
                commentsCount = if (index <= 3) (index + 1) else 0,
                isLiked = index % 3 == 0,
                pleasureCategory = category,
                pleasureTitle = when (index) {
                    0 -> "Méditation guidée"
                    1 -> "Footing du matin"
                    2 -> "Lecture du moment"
                    3 -> "Séance de yoga"
                    4 -> "Pause café"
                    else -> "Moment créatif du jour"
                },
                comments = generateMockComments(index)
            )
        }
    }

    private fun generateMockComments(postIndex: Int): List<PostComment> {
        if (postIndex > 3) return emptyList()

        val commentContents = listOf(
            "Super ! J'adore cette activité 💚",
            "Merci pour l'inspiration !",
            "Ça donne envie d'essayer",
            "Belle photo !",
            "Je vais tester ça demain",
            "Trop bien ! Continue comme ça",
            "C'est exactement ce dont j'avais besoin aujourd'hui",
            "Bravo pour ta régularité !"
        )

        val users = listOf(
            Triple("user1", "Sophie Martin", "@sophie.m"),
            Triple("user2", "Thomas Dubois", "@thomas.d"),
            Triple("user3", "Emma Laurent", "@emma.l"),
            Triple("user4", "Lucas Bernard", "@lucas.b")
        )

        return (0 until (postIndex + 1).coerceAtMost(3)).map { index ->
            val user = users.random()
            PostComment(
                id = "comment_${postIndex}_$index",
                userId = user.first,
                username = user.second,
                userHandle = user.third,
                avatarUrl = null,
                content = commentContents.random(),
                timestamp = System.currentTimeMillis() - ((postIndex + index) * 1800000L)
            )
        }.sortedBy { it.timestamp }
    }


    private fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            val updatedPosts = _uiState.value.friendsPosts.map { post ->
                if (post.id == postId) {
                    val newComment = PostComment(
                        id = "comment_${System.currentTimeMillis()}",
                        userId = "current_user",
                        username = "Moi",
                        userHandle = "@moi",
                        avatarUrl = null,
                        content = content,
                        timestamp = System.currentTimeMillis()
                    )
                    post.copy(
                        comments = post.comments + newComment,
                        commentsCount = post.commentsCount + 1
                    )
                } else {
                    post
                }
            }
            _uiState.update { it.copy(friendsPosts = updatedPosts) }
        }
    }


    override fun onCleared() {
        super.onCleared()
        _publicProfiles.clear()
    }
}
