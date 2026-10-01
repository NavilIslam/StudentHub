package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.TaskEntity
import com.navil.studenthub.data.repository.ClassRepository
import com.navil.studenthub.data.repository.ExamRepository
import com.navil.studenthub.data.repository.GpaRepository
import com.navil.studenthub.data.repository.StudyRepository
import com.navil.studenthub.data.repository.TaskRepository
import com.navil.studenthub.data.repository.UserPreferencesRepository
import com.navil.studenthub.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class HomeUiState(
    val studentName: String = "",
    val greetingPrefix: String = "GOOD MORNING",
    val formattedDate: String = "",
    val todayClasses: List<ClassEntity> = emptyList(),
    val nextClass: ClassEntity? = null,
    val isNextClassActive: Boolean = false,
    val nextClassCountdown: String? = null,
    val upcomingTasks: List<TaskEntity> = emptyList(),
    val todayClassesCount: Int = 0,
    val pendingTasksCount: Int = 0,
    val todayStudyMinutes: Long = 0,
    val dailyStudyGoalMinutes: Int = 240,
    val studyStreakDays: Int = 0,
    val cgpa: Double = 0.0,
    val totalCredits: Double = 0.0,
    val totalCourses: Int = 0,
    val nearestUpcomingExam: ExamWithDetails? = null,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val classRepository: ClassRepository,
    private val taskRepository: TaskRepository,
    private val studyRepository: StudyRepository,
    private val gpaRepository: GpaRepository,
    private val examRepository: ExamRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private fun getGreetingPrefix(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 4..11 -> "GOOD MORNING"
            in 12..16 -> "GOOD AFTERNOON"
            in 17..22 -> "GOOD EVENING"
            else -> "HELLO"
        }
    }

    private val studyStreak: StateFlow<Int> = studyRepository.getAllSessions().map { sessions ->
        if (sessions.isEmpty()) 0
        else {
            val uniqueDates = sessions.map {
                Instant.ofEpochMilli(it.timestampEpochMs)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }.distinct().sortedDescending()

            val today = LocalDate.now()
            var streak = 0
            var checkDate = if (uniqueDates.contains(today)) today else today.minusDays(1)

            if (!uniqueDates.contains(checkDate)) {
                0
            } else {
                for (date in uniqueDates) {
                    if (date == checkDate) {
                        streak++
                        checkDate = checkDate.minusDays(1)
                    } else if (date < checkDate) {
                        break
                    }
                }
                streak
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    private val profileAndPreferences = combine(
        userPreferencesRepository.studentName,
        userPreferencesRepository.dailyStudyGoalMinutes
    ) { name, goal -> name to goal }

    private val classAndTaskOverview = combine(
        classRepository.getClassesForDay(DateUtils.getTodayAcademicDay()),
        taskRepository.getAllTasks(),
        taskRepository.getUpcomingTasks(limit = 3)
    ) { classes, allTasks, upcomingTasks ->
        val pendingCount = allTasks.count { !it.isCompleted }
        val currentMinutes = DateUtils.getCurrentTimeMinutes()
        val todayDay = DateUtils.getTodayAcademicDay()

        val activeClass = classes.firstOrNull {
            it.startTimeMinutes <= currentMinutes && it.endTimeMinutes >= currentMinutes
        }
        val nextClass = activeClass ?: classes.firstOrNull { it.startTimeMinutes > currentMinutes }
        val isCurrentlyActive = activeClass != null

        val countdown = if (nextClass != null) {
            DateUtils.getClassCountdownText(
                dayOfWeek = todayDay,
                startTimeMinutes = nextClass.startTimeMinutes,
                endTimeMinutes = nextClass.endTimeMinutes
            )
        } else null

        ClassTaskOverview(
            todayClasses = classes,
            nextClass = nextClass,
            isNextClassActive = isCurrentlyActive,
            nextClassCountdown = countdown,
            upcomingTasks = upcomingTasks,
            todayClassesCount = classes.size,
            pendingTasksCount = pendingCount
        )
    }

    private data class ClassTaskOverview(
        val todayClasses: List<ClassEntity>,
        val nextClass: ClassEntity?,
        val isNextClassActive: Boolean,
        val nextClassCountdown: String?,
        val upcomingTasks: List<TaskEntity>,
        val todayClassesCount: Int,
        val pendingTasksCount: Int
    )

    private val gpaSummaryFlow = gpaRepository.getCgpaSummary()

    private val nearestExamFlow = combine(
        examRepository.getAllExams(),
        gpaRepository.getAllCourses()
    ) { exams, courses ->
        val today = DateUtils.getTodayEpochDay()
        val upcoming = exams
            .filter { !it.isCompleted && it.dateEpochDay >= today }
            .minWithOrNull(compareBy({ it.dateEpochDay }, { it.startTimeMinutes }))

        if (upcoming != null) {
            val course = upcoming.courseId?.let { id -> courses.firstOrNull { it.id == id }?.courseName }
            ExamWithDetails(upcoming, course, null)
        } else {
            null
        }
    }

    private val studyOverviewFlow = combine(
        studyRepository.getTodayTotalMinutes(),
        studyStreak
    ) { minutes, streak -> minutes to streak }

    val uiState: StateFlow<HomeUiState> = combine(
        profileAndPreferences,
        classAndTaskOverview,
        studyOverviewFlow,
        gpaSummaryFlow,
        nearestExamFlow
    ) { profile, overview, study, gpaResult, nearestExam ->
        HomeUiState(
            studentName = profile.first,
            greetingPrefix = getGreetingPrefix(),
            formattedDate = DateUtils.getFormattedTodayDate(),
            todayClasses = overview.todayClasses,
            nextClass = overview.nextClass,
            isNextClassActive = overview.isNextClassActive,
            nextClassCountdown = overview.nextClassCountdown,
            upcomingTasks = overview.upcomingTasks,
            todayClassesCount = overview.todayClassesCount,
            pendingTasksCount = overview.pendingTasksCount,
            todayStudyMinutes = study.first,
            dailyStudyGoalMinutes = profile.second,
            studyStreakDays = study.second,
            cgpa = gpaResult.gpa,
            totalCredits = gpaResult.totalCredits,
            totalCourses = gpaResult.totalCourses,
            nearestUpcomingExam = nearestExam,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(
            greetingPrefix = getGreetingPrefix(),
            formattedDate = DateUtils.getFormattedTodayDate()
        )
    )

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletion(task.id, !task.isCompleted)
        }
    }
}
