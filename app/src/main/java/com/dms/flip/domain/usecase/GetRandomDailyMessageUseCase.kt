package com.dms.flip.domain.usecase

import com.dms.flip.domain.repository.DailyMessageRepository
import javax.inject.Inject

class GetRandomDailyMessageUseCase @Inject constructor(
    private val repository: DailyMessageRepository
) {
    operator fun invoke() = repository.getRandomDailyMessage()
}