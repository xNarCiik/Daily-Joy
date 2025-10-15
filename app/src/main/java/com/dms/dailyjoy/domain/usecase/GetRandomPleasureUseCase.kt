package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.domain.repository.PleasureRepository
import javax.inject.Inject

class GetRandomPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    fun invoke() = repository.getRandomPleasure()
}