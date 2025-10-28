
package com.dms.flip.domain.usecase.social

import com.dms.flip.domain.repository.SocialRepository
import com.dms.flip.ui.social.Friend
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    operator fun invoke(): Flow<List<Friend>> = socialRepository.getFriends()
}
