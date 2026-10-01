package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.data.repository.ExamRepository
import com.navil.studenthub.data.repository.GpaRepository
import com.navil.studenthub.data.repository.SemesterRepository
import com.navil.studenthub.model.ExamFilter
import com.navil.studenthub.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ExamWithDetails(
    val exam: ExamEntity,
    val courseName: String? = null,
    val semesterName: String? = null
)

data class ExamCounts(
    val allCount: Int = 0,
    val upcomingCount: Int = 0,
    val todayCount: Int = 0,
    val completedCount: Int = 0
)

data class ExamFormState(
    val id: Long? = null,
    val title: String = "",
    val courseId: Long? = null,
    val semesterId: Long? = null,
    val dateEpochDay: Long = LocalDate.now().toEpochDay(),
    val startTimeMinutes: Int = 10 * 60,
    val endTimeMinutes: Int? = 12 * 60,
    val room: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

class ExamViewModel(
    private val examRepository: ExamRepository,
    private val gpaRepository: GpaRepository,
    private val semesterRepository: SemesterRepository
) : ViewModel() {

    private val _currentFilter = MutableStateFlow(ExamFilter.ALL)
    val currentFilter: StateFlow<ExamFilter> = _currentFilter.asStateFlow()

    val availableCourses: StateFlow<List<GpaCourseEntity>> = gpaRepository.getAllCourses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val availableSemesters: StateFlow<List<SemesterEntity>> = semesterRepository.getAllSemesters()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val allExamsFlow = examRepository.getAllExams()

    val examCounts: StateFlow<ExamCounts> = allExamsFlow.combine(_currentFilter) { exams, _ ->
        val today = DateUtils.getTodayEpochDay()
        ExamCounts(
            allCount = exams.size,
            upcomingCount = exams.count { !it.isCompleted && it.dateEpochDay >= today },
            todayCount = exams.count { it.dateEpochDay == today },
            completedCount = exams.count { it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExamCounts()
    )

    val filteredExamsWithDetails: StateFlow<List<ExamWithDetails>> = combine(
        allExamsFlow,
        availableCourses,
        availableSemesters,
        _currentFilter
    ) { exams, courses, semesters, filter ->
        val today = DateUtils.getTodayEpochDay()
        val filtered = when (filter) {
            ExamFilter.ALL -> exams
            ExamFilter.UPCOMING -> exams.filter { !it.isCompleted && it.dateEpochDay >= today }
            ExamFilter.TODAY -> exams.filter { it.dateEpochDay == today }
            ExamFilter.COMPLETED -> exams.filter { it.isCompleted }
        }

        val courseMap = courses.associateBy { it.id }
        val semesterMap = semesters.associateBy { it.id }

        filtered.map { exam ->
            ExamWithDetails(
                exam = exam,
                courseName = exam.courseId?.let { courseMap[it]?.courseName },
                semesterName = exam.semesterId?.let { semesterMap[it]?.name }
            )
        }.sortedWith(
            compareBy<ExamWithDetails> { it.exam.isCompleted }
                .thenBy { it.exam.dateEpochDay }
                .thenBy { it.exam.startTimeMinutes }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val nearestUpcomingExam: StateFlow<ExamWithDetails?> = combine(
        allExamsFlow,
        availableCourses,
        availableSemesters
    ) { exams, courses, semesters ->
        val today = DateUtils.getTodayEpochDay()
        val upcoming = exams
            .filter { !it.isCompleted && it.dateEpochDay >= today }
            .minWithOrNull(compareBy({ it.dateEpochDay }, { it.startTimeMinutes }))

        if (upcoming != null) {
            val course = upcoming.courseId?.let { id -> courses.firstOrNull { it.id == id }?.courseName }
            val semester = upcoming.semesterId?.let { id -> semesters.firstOrNull { it.id == id }?.name }
            ExamWithDetails(upcoming, course, semester)
        } else {
            null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Form & Dialog States
    private val _formState = MutableStateFlow(ExamFormState())
    val formState: StateFlow<ExamFormState> = _formState.asStateFlow()

    private val _isAddEditOpen = MutableStateFlow(false)
    val isAddEditOpen: StateFlow<Boolean> = _isAddEditOpen.asStateFlow()

    private val _selectedExamForDetail = MutableStateFlow<ExamWithDetails?>(null)
    val selectedExamForDetail: StateFlow<ExamWithDetails?> = _selectedExamForDetail.asStateFlow()

    private val _isDetailOpen = MutableStateFlow(false)
    val isDetailOpen: StateFlow<Boolean> = _isDetailOpen.asStateFlow()

    private val _examToDelete = MutableStateFlow<ExamWithDetails?>(null)
    val examToDelete: StateFlow<ExamWithDetails?> = _examToDelete.asStateFlow()

    fun setFilter(filter: ExamFilter) {
        _currentFilter.value = filter
    }

    fun openAddDialog(defaultDateEpochDay: Long? = null) {
        val currentSemId = availableSemesters.value.firstOrNull { it.isCurrent }?.id
            ?: availableSemesters.value.firstOrNull()?.id

        _formState.value = ExamFormState(
            dateEpochDay = defaultDateEpochDay ?: LocalDate.now().toEpochDay(),
            semesterId = currentSemId
        )
        _isAddEditOpen.value = true
    }

    fun openEditDialog(examWithDetails: ExamWithDetails) {
        val exam = examWithDetails.exam
        _formState.value = ExamFormState(
            id = exam.id,
            title = exam.title,
            courseId = exam.courseId,
            semesterId = exam.semesterId,
            dateEpochDay = exam.dateEpochDay,
            startTimeMinutes = exam.startTimeMinutes,
            endTimeMinutes = exam.endTimeMinutes,
            room = exam.room,
            notes = exam.notes,
            isCompleted = exam.isCompleted
        )
        _isAddEditOpen.value = true
        _isDetailOpen.value = false
    }

    fun openDetailDialog(examWithDetails: ExamWithDetails) {
        _selectedExamForDetail.value = examWithDetails
        _isDetailOpen.value = true
    }

    fun dismissAddEditDialog() {
        _isAddEditOpen.value = false
        _formState.value = ExamFormState()
    }

    fun dismissDetailDialog() {
        _isDetailOpen.value = false
        _selectedExamForDetail.value = null
    }

    fun updateFormField(transform: ExamFormState.() -> ExamFormState) {
        _formState.update { it.transform().copy(errorMessage = null) }
    }

    fun saveExam(): Boolean {
        val form = _formState.value
        if (form.title.isBlank()) {
            _formState.update { it.copy(errorMessage = "Exam title is required") }
            return false
        }

        if (form.endTimeMinutes != null && form.endTimeMinutes <= form.startTimeMinutes) {
            _formState.update { it.copy(errorMessage = "End time must be after start time") }
            return false
        }

        viewModelScope.launch {
            if (form.id == null || form.id == 0L) {
                examRepository.insertExam(
                    title = form.title,
                    courseId = form.courseId,
                    semesterId = form.semesterId,
                    dateEpochDay = form.dateEpochDay,
                    startTimeMinutes = form.startTimeMinutes,
                    endTimeMinutes = form.endTimeMinutes,
                    room = form.room,
                    notes = form.notes
                )
            } else {
                examRepository.updateExam(
                    id = form.id,
                    title = form.title,
                    courseId = form.courseId,
                    semesterId = form.semesterId,
                    dateEpochDay = form.dateEpochDay,
                    startTimeMinutes = form.startTimeMinutes,
                    endTimeMinutes = form.endTimeMinutes,
                    room = form.room,
                    notes = form.notes,
                    isCompleted = form.isCompleted
                )
            }
            dismissAddEditDialog()
        }
        return true
    }

    fun toggleExamCompletion(exam: ExamEntity) {
        viewModelScope.launch {
            examRepository.updateExamCompletion(exam.id, !exam.isCompleted)
            // Update detail state if open
            if (_selectedExamForDetail.value?.exam?.id == exam.id) {
                _selectedExamForDetail.update { detail ->
                    detail?.copy(exam = detail.exam.copy(isCompleted = !exam.isCompleted))
                }
            }
        }
    }

    fun requestDeleteExam(examWithDetails: ExamWithDetails) {
        _examToDelete.value = examWithDetails
    }

    fun confirmDeleteExam() {
        val toDelete = _examToDelete.value ?: return
        viewModelScope.launch {
            examRepository.deleteExam(toDelete.exam)
            _examToDelete.value = null
            if (_isDetailOpen.value && _selectedExamForDetail.value?.exam?.id == toDelete.exam.id) {
                dismissDetailDialog()
            }
            if (_isAddEditOpen.value && _formState.value.id == toDelete.exam.id) {
                dismissAddEditDialog()
            }
        }
    }

    fun cancelDeleteExam() {
        _examToDelete.value = null
    }
}
