package com.dms.dailyjoy.domain.usecase.pleasures

import com.dms.dailyjoy.domain.repository.PleasureRepository
import javax.inject.Inject

class GetPleasuresUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke() = repository.getAllPleasures()
}
