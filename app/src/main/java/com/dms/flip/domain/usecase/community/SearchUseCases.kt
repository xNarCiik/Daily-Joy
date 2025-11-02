package com.dms.flip.domain.usecase.community

import com.dms.flip.domain.model.community.PublicProfile
import com.dms.flip.domain.model.community.UserSearchResult
import com.dms.flip.domain.repository.community.ProfileRepository
import com.dms.flip.domain.repository.community.SearchRepository
import com.dms.flip.domain.util.Result
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String, limit: Int = 20): Result<List<UserSearchResult>> =
        runCatching { searchRepository.searchUsers(query, limit) }
            .fold(
                onSuccess = { Result.Ok(it) },
                onFailure = { Result.Err(it) }
            )
}

class GetPublicProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<PublicProfile> =
        runCatching { profileRepository.getPublicProfile(userId) }
            .fold(
                onSuccess = { Result.Ok(it) },
                onFailure = { Result.Err(it) }
            )
}
