package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRandomPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    /**
     * Invokes the use case to get a random pleasure.
     * It collects all pleasures from the repository and returns a random one.
     * If the list is empty, it returns null.
     */
    operator fun invoke() = repository.getAllPleasures().map { pleasures ->
        pleasures.randomOrNull()
    }
}
