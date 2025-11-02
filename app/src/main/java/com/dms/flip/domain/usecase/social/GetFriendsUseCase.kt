
package com.dms.flip.domain.usecase.social

import com.dms.flip.domain.model.community.Friend
import com.dms.flip.domain.repository.SocialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    operator fun invoke(): Flow<List<Friend>> = socialRepository.getFriends()
}
