
package com.dms.dailyjoy.domain.usecase.social

import com.dms.dailyjoy.domain.repository.SocialRepository
import com.dms.dailyjoy.ui.social.Friend
import javax.inject.Inject

class RemoveFriendUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    suspend operator fun invoke(friend: Friend) = socialRepository.removeFriend(friend)
}
