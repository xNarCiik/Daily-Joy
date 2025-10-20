package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.data.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: Theme) = settingsRepository.setTheme(theme)
}
