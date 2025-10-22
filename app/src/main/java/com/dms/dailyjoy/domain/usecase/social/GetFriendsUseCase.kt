
package com.dms.dailyjoy.domain.usecase.social

import com.dms.dailyjoy.domain.repository.SocialRepository
import com.dms.dailyjoy.ui.social.Friend
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) {
    operator fun invoke(): Flow<List<Friend>> = socialRepository.getFriends()
}
