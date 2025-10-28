package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.data.model.Pleasure
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.data.model.PleasureType
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class AddCustomPleasureUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(title: String, description: String, category: PleasureCategory, type: PleasureType) {
        val pleasure = Pleasure(
            title = title,
            description = description,
            type = type,
            category = category,
            isCustom = true,
            isEnabled = true
        )
        pleasureRepository.insert(pleasure)
    }
}
