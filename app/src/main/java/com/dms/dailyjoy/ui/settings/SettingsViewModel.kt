package com.dms.dailyjoy.ui.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.usecase.GetDailyReminderStateUseCase
import com.dms.dailyjoy.domain.usecase.GetReminderTimeUseCase
import com.dms.dailyjoy.domain.usecase.GetThemeUseCase
import com.dms.dailyjoy.domain.usecase.SetDailyReminderStateUseCase
import com.dms.dailyjoy.domain.usecase.SetReminderTimeUseCase
import com.dms.dailyjoy.domain.usecase.SetThemeUseCase
import com.dms.dailyjoy.notification.DailyReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    getDailyReminderStateUseCase: GetDailyReminderStateUseCase,
    private val setDailyReminderStateUseCase: SetDailyReminderStateUseCase,
    getReminderTimeUseCase: GetReminderTimeUseCase,
    private val setReminderTimeUseCase: SetReminderTimeUseCase
) : ViewModel() {

    private val dailyReminderManager = DailyReminderManager(application)

    val theme: StateFlow<Theme> = getThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Theme.SYSTEM)

    val dailyReminderEnabled: StateFlow<Boolean> = getDailyReminderStateUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val reminderTime: StateFlow<String> = getReminderTimeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "08:00")

    fun onThemeChange(theme: Theme) = viewModelScope.launch {
        setThemeUseCase(theme)
    }

    fun onDailyReminderEnabledChange(enabled: Boolean) = viewModelScope.launch {
        setDailyReminderStateUseCase(enabled)
        if (enabled) {
            dailyReminderManager.schedule(reminderTime.value)
        } else {
            dailyReminderManager.cancel()
        }
    }

    fun onReminderTimeChange(time: String) = viewModelScope.launch {
        setReminderTimeUseCase(time)
        dailyReminderManager.schedule(time)
    }
}
