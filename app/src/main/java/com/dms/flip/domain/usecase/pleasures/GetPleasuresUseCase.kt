package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class GetPleasuresUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke() = repository.getAllPleasures()
}
