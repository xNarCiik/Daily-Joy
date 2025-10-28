
package com.dms.flip.domain.usecase.social

import com.dms.flip.domain.repository.SocialRepository
import com.dms.flip.ui.social.Friend
import javax.inject.Inject

class RemoveFriendUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    suspend operator fun invoke(friend: Friend) = socialRepository.removeFriend(friend)
}
