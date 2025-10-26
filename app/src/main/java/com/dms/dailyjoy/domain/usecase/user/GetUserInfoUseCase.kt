package com.dms.dailyjoy.domain.usecase.user

import com.dms.dailyjoy.domain.model.UserInfo
import com.dms.dailyjoy.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserInfo?> = userRepository.getUserInfo()
}