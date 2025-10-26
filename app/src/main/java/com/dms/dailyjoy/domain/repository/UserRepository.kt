package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserInfo(): Flow<UserInfo?>
}
