package com.dms.dailyjoy.domain.usecase.settings

import com.dms.dailyjoy.domain.repository.SettingsRepository
import com.dms.dailyjoy.domain.model.Theme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Theme> = settingsRepository.theme
}
