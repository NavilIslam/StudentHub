package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.StudySessionEntity
import com.navil.studenthub.data.repository.StudyRepository
import com.navil.studenthub.data.repository.UserPreferencesRepository
import com.navil.studenthub.model.TimerMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class StudyUiState(
    val timerMode: TimerMode = TimerMode.STUDY,
    val remainingSeconds: Long = 25 * 60,
    val totalSecondsForCurrentMode: Long = 25 * 60,
    val isRunning: Boolean = false,
    val currentSubject: String = "General Study",
    val completedSessionsToday: Int = 0,
    val todayStudyMinutes: Long = 0,
    val thisWeekStudyMinutes: Long = 0,
    val studyStreakDays: Int = 0,
    val studyDurationMinutes: Int = 25,
    val shortBreakDurationMinutes: Int = 5,
    val longBreakDurationMinutes: Int = 15,
    val sessionsCompletedInCycle: Int = 0,
    val recentSessions: List<StudySessionEntity> = emptyList()
)

class StudyViewModel(
    private val studyRepository: StudyRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _timerMode = MutableStateFlow(TimerMode.STUDY)
    private val _remainingSeconds = MutableStateFlow(25L * 60)
    private val _totalSeconds = MutableStateFlow(25L * 60)
    private val _isRunning = MutableStateFlow(false)
    private val _sessionsCompletedInCycle = MutableStateFlow(0)
    private val _currentSubject = MutableStateFlow("General Study")

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            val studyDuration = userPreferencesRepository.defaultStudyDurationMinutes.first()
            _remainingSeconds.value = studyDuration.toLong() * 60
            _totalSeconds.value = studyDuration.toLong() * 60
        }
    }

    private val studyStreak: StateFlow<Int> = studyRepository.getAllSessions().map { sessions ->
        calculateStreak(sessions)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    private val allSessionsFlow = studyRepository.getAllSessions()

    private data class TimerRuntimeState(
        val mode: TimerMode,
        val remaining: Long,
        val total: Long,
        val running: Boolean,
        val cycleCount: Int,
        val subject: String
    )

    private data class StatsState(
        val todayMins: Long,
        val weekMins: Long,
        val todayCount: Int,
        val streak: Int,
        val recent: List<StudySessionEntity>
    )

    private val timerCoreFlow = combine(
        _timerMode,
        _remainingSeconds,
        _totalSeconds
    ) { mode, remaining, total -> Triple(mode, remaining, total) }

    private val timerMetaFlow = combine(
        _isRunning,
        _sessionsCompletedInCycle,
        _currentSubject
    ) { running, cycleCount, subject -> Triple(running, cycleCount, subject) }

    private val timerRuntimeFlow = combine(
        timerCoreFlow,
        timerMetaFlow
    ) { core, meta ->
        TimerRuntimeState(
            mode = core.first,
            remaining = core.second,
            total = core.third,
            running = meta.first,
            cycleCount = meta.second,
            subject = meta.third
        )
    }

    private val statsFlow = combine(
        studyRepository.getTodayTotalMinutes(),
        studyRepository.getThisWeekTotalMinutes(),
        studyRepository.getTodaySessionCount(),
        studyStreak,
        allSessionsFlow
    ) { todayMins, weekMins, todayCount, streak, sessions ->
        StatsState(todayMins, weekMins, todayCount, streak, sessions.take(10))
    }

    val uiState: StateFlow<StudyUiState> = combine(
        timerRuntimeFlow,
        statsFlow
    ) { timer, stats ->
        StudyUiState(
            timerMode = timer.mode,
            remainingSeconds = timer.remaining,
            totalSecondsForCurrentMode = timer.total,
            isRunning = timer.running,
            currentSubject = timer.subject,
            completedSessionsToday = stats.todayCount,
            todayStudyMinutes = stats.todayMins,
            thisWeekStudyMinutes = stats.weekMins,
            studyStreakDays = stats.streak,
            sessionsCompletedInCycle = timer.cycleCount,
            recentSessions = stats.recent
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StudyUiState()
    )

    fun setSubject(subject: String) {
        _currentSubject.value = if (subject.isBlank()) "General Study" else subject.trim()
    }

    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true

        timerJob = viewModelScope.launch {
            while (_isRunning.value && _remainingSeconds.value > 0) {
                delay(1000)
                _remainingSeconds.update { (it - 1).coerceAtLeast(0) }
            }

            if (_remainingSeconds.value == 0L && _isRunning.value) {
                _isRunning.value = false
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        pauseTimer()
        viewModelScope.launch {
            setMode(_timerMode.value)
        }
    }

    fun skipMode() {
        pauseTimer()
        viewModelScope.launch {
            when (_timerMode.value) {
                TimerMode.STUDY -> {
                    val nextCycle = _sessionsCompletedInCycle.value + 1
                    _sessionsCompletedInCycle.value = nextCycle
                    if (nextCycle % 4 == 0) {
                        setMode(TimerMode.LONG_BREAK)
                    } else {
                        setMode(TimerMode.SHORT_BREAK)
                    }
                }
                TimerMode.SHORT_BREAK, TimerMode.LONG_BREAK -> {
                    setMode(TimerMode.STUDY)
                }
            }
        }
    }

    private suspend fun onTimerFinished() {
        if (_timerMode.value == TimerMode.STUDY) {
            val studyDurationMins = (_totalSeconds.value / 60)
            studyRepository.insertSession(
                durationMinutes = studyDurationMins,
                sessionType = "Study",
                subject = _currentSubject.value
            )
            val nextCycle = _sessionsCompletedInCycle.value + 1
            _sessionsCompletedInCycle.value = nextCycle

            if (nextCycle % 4 == 0) {
                setMode(TimerMode.LONG_BREAK)
            } else {
                setMode(TimerMode.SHORT_BREAK)
            }
        } else {
            setMode(TimerMode.STUDY)
        }
    }

    fun setMode(mode: TimerMode) {
        pauseTimer()
        _timerMode.value = mode
        viewModelScope.launch {
            val minutes = when (mode) {
                TimerMode.STUDY -> userPreferencesRepository.defaultStudyDurationMinutes.first()
                TimerMode.SHORT_BREAK -> userPreferencesRepository.defaultShortBreakDurationMinutes.first()
                TimerMode.LONG_BREAK -> userPreferencesRepository.defaultLongBreakDurationMinutes.first()
            }
            val seconds = minutes.toLong() * 60
            _remainingSeconds.value = seconds
            _totalSeconds.value = seconds
        }
    }

    private fun calculateStreak(sessions: List<StudySessionEntity>): Int {
        if (sessions.isEmpty()) return 0

        val uniqueDates = sessions.map {
            Instant.ofEpochMilli(it.timestampEpochMs)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }.distinct().sortedDescending()

        val today = LocalDate.now()
        var streak = 0
        var checkDate = if (uniqueDates.contains(today)) today else today.minusDays(1)

        if (!uniqueDates.contains(checkDate)) {
            return 0
        }

        for (date in uniqueDates) {
            if (date == checkDate) {
                streak++
                checkDate = checkDate.minusDays(1)
            } else if (date < checkDate) {
                break
            }
        }
        return streak
    }
}
