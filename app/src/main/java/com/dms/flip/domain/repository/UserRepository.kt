package com.dms.flip.domain.repository

import com.dms.flip.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserInfo(): Flow<UserInfo?>
}
