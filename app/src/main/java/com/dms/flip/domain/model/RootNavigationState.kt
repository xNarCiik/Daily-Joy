package com.dms.flip.domain.model

sealed interface RootNavigationState {
    object Loading : RootNavigationState
    object AuthenticatedAndOnboarded : RootNavigationState
    object AuthenticatedButNotOnboarded : RootNavigationState
    object NotAuthenticated : RootNavigationState
}
