package com.navil.studenthub.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.navil.studenthub.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserPreferencesRepository(private val context: Context) {

    private object PreferenceKeys {
        val STUDENT_NAME = stringPreferencesKey("student_name")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DAILY_STUDY_GOAL_MINUTES = intPreferencesKey("daily_study_goal_minutes")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DEFAULT_STUDY_DURATION = intPreferencesKey("default_study_duration")
        val DEFAULT_SHORT_BREAK_DURATION = intPreferencesKey("default_short_break_duration")
        val DEFAULT_LONG_BREAK_DURATION = intPreferencesKey("default_long_break_duration")
    }

    val studentName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.STUDENT_NAME] ?: ""
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val raw = preferences[PreferenceKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        ThemeMode.fromString(raw)
    }

    val dailyStudyGoalMinutes: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.DAILY_STUDY_GOAL_MINUTES] ?: 240 // 4 hours default
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val defaultStudyDurationMinutes: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.DEFAULT_STUDY_DURATION] ?: 25
    }

    val defaultShortBreakDurationMinutes: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.DEFAULT_SHORT_BREAK_DURATION] ?: 5
    }

    val defaultLongBreakDurationMinutes: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.DEFAULT_LONG_BREAK_DURATION] ?: 15
    }

    suspend fun setStudentName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.STUDENT_NAME] = name.trim()
        }
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun setDailyStudyGoalMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DAILY_STUDY_GOAL_MINUTES] = minutes.coerceIn(15, 720)
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setDefaultStudyDurationMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DEFAULT_STUDY_DURATION] = minutes.coerceIn(5, 120)
        }
    }

    suspend fun setDefaultShortBreakDurationMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DEFAULT_SHORT_BREAK_DURATION] = minutes.coerceIn(1, 30)
        }
    }

    suspend fun setDefaultLongBreakDurationMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DEFAULT_LONG_BREAK_DURATION] = minutes.coerceIn(5, 60)
        }
    }
}
