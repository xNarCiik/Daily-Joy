package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.data.local.LocalPleasureDataSource
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class GetPleasuresUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke() = repository.getPleasures()
}

class GetLocalPleasuresUseCase @Inject constructor(
    private val localPleasureDataSource: LocalPleasureDataSource
) {
    operator fun invoke() = localPleasureDataSource.getPleasures()
}
