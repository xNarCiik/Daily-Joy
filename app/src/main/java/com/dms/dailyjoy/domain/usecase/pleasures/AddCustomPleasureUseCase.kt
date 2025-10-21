package com.dms.dailyjoy.domain.usecase.pleasures

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.domain.repository.PleasureRepository
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
