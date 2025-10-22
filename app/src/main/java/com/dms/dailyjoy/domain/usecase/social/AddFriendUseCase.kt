
package com.dms.dailyjoy.domain.usecase.social

import com.dms.dailyjoy.domain.repository.SocialRepository
import javax.inject.Inject

class AddFriendUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    suspend operator fun invoke(username: String) = socialRepository.addFriend(username)
}
