
package com.dms.flip.domain.usecase.social

import com.dms.flip.domain.model.community.Friend
import com.dms.flip.domain.repository.SocialRepository
import javax.inject.Inject

class RemoveFriendUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    suspend operator fun invoke(friend: Friend) = socialRepository.removeFriend(friend)
}
