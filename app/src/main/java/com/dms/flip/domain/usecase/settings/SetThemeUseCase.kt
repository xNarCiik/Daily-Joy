package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.model.Theme
import com.dms.flip.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: Theme) = settingsRepository.setTheme(theme)
}
