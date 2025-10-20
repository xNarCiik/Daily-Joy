package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRandomPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    /**
     * Invokes the use case to get a random enabled pleasure.
     * It collects all pleasures from the repository, filters the enabled ones, and returns a random one.
     * If the list is empty, it returns null.
     */
    suspend operator fun invoke(): Pleasure? {
        return repository.getAllPleasures().first().filter { it.isEnabled }.randomOrNull()
    }
}
