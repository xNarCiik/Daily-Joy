package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.repository.SettingsRepository
import com.dms.flip.domain.model.Theme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Theme> = settingsRepository.theme
}
