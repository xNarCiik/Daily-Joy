package com.dms.dailyjoy.domain.usecase.settings

import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: Theme) = settingsRepository.setTheme(theme)
}
