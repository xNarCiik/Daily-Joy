package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import javax.inject.Inject

class GetRandomDailyMessageUseCase @Inject constructor(
    private val repository: DailyMessageRepository
) {
    fun invoke() = repository.getRandomDailyMessage()
}