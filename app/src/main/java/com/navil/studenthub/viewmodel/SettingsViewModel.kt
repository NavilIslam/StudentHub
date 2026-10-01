package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.repository.UserPreferencesRepository
import com.navil.studenthub.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val studentName: String = "",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dailyStudyGoalMinutes: Int = 240,
    val notificationsEnabled: Boolean = true,
    val defaultStudyDurationMinutes: Int = 25,
    val defaultShortBreakDurationMinutes: Int = 5,
    val defaultLongBreakDurationMinutes: Int = 15
)

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val profileAndThemeFlow = combine(
        userPreferencesRepository.studentName,
        userPreferencesRepository.themeMode,
        userPreferencesRepository.notificationsEnabled
    ) { name, theme, notif ->
        Triple(name, theme, notif)
    }

    private val durationsFlow = combine(
        userPreferencesRepository.dailyStudyGoalMinutes,
        userPreferencesRepository.defaultStudyDurationMinutes,
        userPreferencesRepository.defaultShortBreakDurationMinutes,
        userPreferencesRepository.defaultLongBreakDurationMinutes
    ) { goal, study, shortBreak, longBreak ->
        DurationSettings(goal, study, shortBreak, longBreak)
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        profileAndThemeFlow,
        durationsFlow
    ) { profile, durations ->
        SettingsUiState(
            studentName = profile.first,
            themeMode = profile.second,
            notificationsEnabled = profile.third,
            dailyStudyGoalMinutes = durations.dailyGoal,
            defaultStudyDurationMinutes = durations.study,
            defaultShortBreakDurationMinutes = durations.shortBreak,
            defaultLongBreakDurationMinutes = durations.longBreak
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateStudentName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.setStudentName(name)
        }
    }

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(themeMode)
        }
    }

    fun updateDailyStudyGoal(minutes: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDailyStudyGoalMinutes(minutes)
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setNotificationsEnabled(enabled)
        }
    }

    fun updateDefaultStudyDuration(minutes: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDefaultStudyDurationMinutes(minutes)
        }
    }

    fun updateDefaultShortBreakDuration(minutes: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDefaultShortBreakDurationMinutes(minutes)
        }
    }

    fun updateDefaultLongBreakDuration(minutes: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDefaultLongBreakDurationMinutes(minutes)
        }
    }

    private data class DurationSettings(
        val dailyGoal: Int,
        val study: Int,
        val shortBreak: Int,
        val longBreak: Int
    )
}
