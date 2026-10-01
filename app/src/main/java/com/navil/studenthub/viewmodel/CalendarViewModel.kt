package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.data.local.entity.TaskEntity
import com.navil.studenthub.data.repository.ClassRepository
import com.navil.studenthub.data.repository.ExamRepository
import com.navil.studenthub.data.repository.GpaRepository
import com.navil.studenthub.data.repository.SemesterRepository
import com.navil.studenthub.data.repository.TaskRepository
import com.navil.studenthub.model.AcademicCalendarEvent
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.model.CalendarEventType
import com.navil.studenthub.model.CalendarEventTypeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val displayedMonth: YearMonth = YearMonth.now(),
    val selectedEventType: CalendarEventTypeFilter = CalendarEventTypeFilter.ALL,
    val selectedSemesterId: Long? = null,
    val availableSemesters: List<SemesterEntity> = emptyList(),
    val eventsForSelectedDate: List<AcademicCalendarEvent> = emptyList(),
    val monthEventDotsMap: Map<LocalDate, List<CalendarEventType>> = emptyMap(),
    val totalEventsCount: Int = 0,
    val classesCount: Int = 0,
    val tasksCount: Int = 0,
    val examsCount: Int = 0,
    val isLoading: Boolean = false
)

class CalendarViewModel(
    private val classRepository: ClassRepository,
    private val taskRepository: TaskRepository,
    private val examRepository: ExamRepository,
    private val gpaRepository: GpaRepository,
    private val semesterRepository: SemesterRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _displayedMonth = MutableStateFlow(YearMonth.now())
    val displayedMonth: StateFlow<YearMonth> = _displayedMonth.asStateFlow()

    private val _selectedEventType = MutableStateFlow(CalendarEventTypeFilter.ALL)
    val selectedEventType: StateFlow<CalendarEventTypeFilter> = _selectedEventType.asStateFlow()

    private val _selectedSemesterId = MutableStateFlow<Long?>(null)
    val selectedSemesterId: StateFlow<Long?> = _selectedSemesterId.asStateFlow()

    val availableSemesters: StateFlow<List<SemesterEntity>> = semesterRepository.getAllSemesters()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val calendarSettingsFlow = combine(
        _selectedDate,
        _displayedMonth,
        _selectedEventType,
        _selectedSemesterId
    ) { date, month, filter, semesterId ->
        CalendarConfig(date, month, filter, semesterId)
    }

    private data class CalendarConfig(
        val selectedDate: LocalDate,
        val displayedMonth: YearMonth,
        val filter: CalendarEventTypeFilter,
        val semesterId: Long?
    )

    private val rawDataFlow = combine(
        classRepository.getAllClasses(),
        taskRepository.getAllTasks(),
        examRepository.getAllExams(),
        gpaRepository.getAllCourses(),
        availableSemesters
    ) { classes, tasks, exams, courses, semesters ->
        RawData(classes, tasks, exams, courses, semesters)
    }

    private data class RawData(
        val classes: List<ClassEntity>,
        val tasks: List<TaskEntity>,
        val exams: List<ExamEntity>,
        val courses: List<GpaCourseEntity>,
        val semesters: List<SemesterEntity>
    )

    val uiState: StateFlow<CalendarUiState> = combine(
        calendarSettingsFlow,
        rawDataFlow
    ) { config, data ->
        val courseMap = data.courses.associateBy { it.id }
        val semesterMap = data.semesters.associateBy { it.id }

        // 1. Build events for the selected date
        val selectedAcademicDay = AcademicDayOfWeek.fromJavaDayOfWeek(config.selectedDate.dayOfWeek)
        val selectedEpochDay = config.selectedDate.toEpochDay()

        // Classes for selected date
        val classEvents = data.classes
            .filter { it.dayOfWeek == selectedAcademicDay }
            .map {
                AcademicCalendarEvent.ClassEvent(
                    classEntity = it,
                    date = config.selectedDate
                )
            }

        // Tasks for selected date
        val taskEvents = data.tasks
            .filter { it.dueDateEpochDay == selectedEpochDay }
            .map {
                AcademicCalendarEvent.TaskEvent(
                    taskEntity = it,
                    date = config.selectedDate
                )
            }

        // Exams for selected date
        val examEvents = data.exams
            .filter { it.dateEpochDay == selectedEpochDay }
            .filter { exam ->
                if (config.semesterId == null) true
                else exam.semesterId == config.semesterId
            }
            .map {
                val course = it.courseId?.let { cid -> courseMap[cid] }
                val semester = it.semesterId?.let { sid -> semesterMap[sid] }
                AcademicCalendarEvent.ExamEvent(
                    examEntity = it,
                    date = config.selectedDate,
                    courseName = course?.courseName,
                    semesterName = semester?.name
                )
            }

        val allDailyEvents = (classEvents + taskEvents + examEvents)

        val totalCount = allDailyEvents.size
        val cCount = classEvents.size
        val tCount = taskEvents.size
        val eCount = examEvents.size

        // Filter by selected event type
        val filteredEvents = when (config.filter) {
            CalendarEventTypeFilter.ALL -> allDailyEvents
            CalendarEventTypeFilter.CLASSES -> classEvents
            CalendarEventTypeFilter.TASKS -> taskEvents
            CalendarEventTypeFilter.EXAMS -> examEvents
        }.sortedWith(
            compareBy<AcademicCalendarEvent> { it.isCompleted }
                .thenBy { it.startTimeMinutes ?: Int.MIN_VALUE }
                .thenBy { it.type.ordinal }
                .thenBy { it.title }
        )

        // 2. Compute event dots for the entire displayed month
        val daysInMonth = config.displayedMonth.lengthOfMonth()
        val dotsMap = mutableMapOf<LocalDate, MutableList<CalendarEventType>>()

        for (day in 1..daysInMonth) {
            val date = config.displayedMonth.atDay(day)
            val academicDay = AcademicDayOfWeek.fromJavaDayOfWeek(date.dayOfWeek)
            val epochDay = date.toEpochDay()

            val dayDots = mutableListOf<CalendarEventType>()

            val hasClasses = data.classes.any { it.dayOfWeek == academicDay }
            if (hasClasses) dayDots.add(CalendarEventType.CLASS)

            val hasTasks = data.tasks.any { it.dueDateEpochDay == epochDay }
            if (hasTasks) dayDots.add(CalendarEventType.TASK)

            val hasExams = data.exams.any { exam ->
                exam.dateEpochDay == epochDay && (config.semesterId == null || exam.semesterId == config.semesterId)
            }
            if (hasExams) dayDots.add(CalendarEventType.EXAM)

            if (dayDots.isNotEmpty()) {
                dotsMap[date] = dayDots
            }
        }

        CalendarUiState(
            selectedDate = config.selectedDate,
            displayedMonth = config.displayedMonth,
            selectedEventType = config.filter,
            selectedSemesterId = config.semesterId,
            availableSemesters = data.semesters,
            eventsForSelectedDate = filteredEvents,
            monthEventDotsMap = dotsMap,
            totalEventsCount = totalCount,
            classesCount = cCount,
            tasksCount = tCount,
            examsCount = eCount,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        if (YearMonth.from(date) != _displayedMonth.value) {
            _displayedMonth.value = YearMonth.from(date)
        }
    }

    fun previousMonth() {
        _displayedMonth.update { it.minusMonths(1) }
    }

    fun nextMonth() {
        _displayedMonth.update { it.plusMonths(1) }
    }

    fun jumpToToday() {
        val today = LocalDate.now()
        _selectedDate.value = today
        _displayedMonth.value = YearMonth.from(today)
    }

    fun setEventTypeFilter(filter: CalendarEventTypeFilter) {
        _selectedEventType.value = filter
    }

    fun setSemesterFilter(semesterId: Long?) {
        _selectedSemesterId.value = semesterId
    }
}
