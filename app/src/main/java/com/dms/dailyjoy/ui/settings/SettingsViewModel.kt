package com.dms.dailyjoy.ui.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.usecase.settings.GetDailyReminderStateUseCase
import com.dms.dailyjoy.domain.usecase.settings.GetReminderTimeUseCase
import com.dms.dailyjoy.domain.usecase.settings.GetThemeUseCase
import com.dms.dailyjoy.domain.usecase.settings.SetDailyReminderStateUseCase
import com.dms.dailyjoy.domain.usecase.settings.SetReminderTimeUseCase
import com.dms.dailyjoy.domain.usecase.settings.SetThemeUseCase
import com.dms.dailyjoy.notification.DailyReminderManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val getDailyReminderStateUseCase: GetDailyReminderStateUseCase,
    private val setDailyReminderStateUseCase: SetDailyReminderStateUseCase,
    private val getReminderTimeUseCase: GetReminderTimeUseCase,
    private val setReminderTimeUseCase: SetReminderTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val dailyReminderManager = DailyReminderManager(application)

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUserInfo()
        loadSettings()
    }

    private fun loadUserInfo() {
        val user = firebaseAuth.currentUser ?: return

        val uid = user.uid
        val email = user.email

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val username = doc.getString("username")
                val avatarUrl = doc.getString("avatar_url")
                _uiState.value = _uiState.value.copy(
                    userInfo = UserInfo(
                        id = uid,
                        username = username,
                        email = email,
                        avatarUrl = avatarUrl
                    )
                )
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun loadSettings() = viewModelScope.launch {
        combine(
            getThemeUseCase(),
            getDailyReminderStateUseCase(),
            getReminderTimeUseCase()
        ) { theme, dailyReminderEnabled, reminderTime ->
            SettingsUiState(
                theme = theme,
                dailyReminderEnabled = dailyReminderEnabled,
                reminderTime = reminderTime
            )
        }.collect { combinedState ->
            _uiState.value = combinedState
        }
    }


    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnThemeChanged -> onThemeChange(event.theme)
            is SettingsEvent.OnDailyReminderEnabledChanged -> onDailyReminderEnabledChange(event.enabled)
            is SettingsEvent.OnReminderTimeChanged -> onReminderTimeChange(event.time)
        }
    }

    private fun onThemeChange(theme: Theme) = viewModelScope.launch {
        setThemeUseCase(theme)
    }

    private fun onDailyReminderEnabledChange(enabled: Boolean) = viewModelScope.launch {
        setDailyReminderStateUseCase(enabled)
        if (enabled) {
            dailyReminderManager.schedule(uiState.value.reminderTime)
        } else {
            dailyReminderManager.cancel()
        }
    }

    private fun onReminderTimeChange(time: String) = viewModelScope.launch {
        setReminderTimeUseCase(time)
        dailyReminderManager.schedule(time)
    }
}
