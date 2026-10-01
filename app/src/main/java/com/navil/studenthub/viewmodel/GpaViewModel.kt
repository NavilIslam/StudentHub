package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.data.repository.GpaRepository
import com.navil.studenthub.data.repository.SemesterRepository
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.util.GpaCalculator
import com.navil.studenthub.util.Validators
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CourseFormState(
    val id: Long? = null,
    val semesterId: Long = 0L,
    val courseName: String = "",
    val creditHoursText: String = "3.0",
    val grade: GpaGrade = GpaGrade.A,
    val errorMessage: String? = null
)

data class SemesterFormState(
    val id: Long? = null,
    val name: String = "",
    val academicYear: String = "2026",
    val semesterNumberText: String = "1",
    val startDate: String = "",
    val endDate: String = "",
    val isCurrent: Boolean = false,
    val errorMessage: String? = null
)

class GpaViewModel(
    private val gpaRepository: GpaRepository,
    private val semesterRepository: SemesterRepository
) : ViewModel() {

    val semesters: StateFlow<List<SemesterEntity>> = semesterRepository.getAllSemesters()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedSemesterId = MutableStateFlow<Long?>(null)
    val selectedSemesterId: StateFlow<Long?> = _selectedSemesterId.asStateFlow()

    init {
        viewModelScope.launch {
            val defaultId = semesterRepository.ensureDefaultSemesterExists()
            if (_selectedSemesterId.value == null && defaultId > 0) {
                _selectedSemesterId.value = defaultId
            }
        }
    }

    val selectedSemester: StateFlow<SemesterEntity?> = combine(
        semesters,
        _selectedSemesterId
    ) { semesterList, selectedId ->
        if (semesterList.isEmpty()) {
            null
        } else {
            semesterList.firstOrNull { it.id == selectedId }
                ?: semesterList.firstOrNull { it.isCurrent }
                ?: semesterList.first()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesForSelectedSemester: StateFlow<List<GpaCourseEntity>> = selectedSemester
        .flatMapLatest { sem ->
            if (sem != null && sem.id > 0) gpaRepository.getCoursesForSemester(sem.id)
            else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedSemesterGpaSummary: StateFlow<GpaCalculator.GpaResult> = selectedSemester
        .flatMapLatest { sem ->
            if (sem != null && sem.id > 0) gpaRepository.getSemesterGpaSummary(sem.id)
            else flowOf(GpaCalculator.GpaResult(0.0, 0.0, 0.0))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GpaCalculator.GpaResult(0.0, 0.0, 0.0)
        )

    val cgpaSummary: StateFlow<GpaCalculator.GpaResult> = gpaRepository.getCgpaSummary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GpaCalculator.GpaResult(0.0, 0.0, 0.0)
        )

    val semesterHistory: StateFlow<List<GpaCalculator.SemesterPerformance>> = gpaRepository.getSemesterPerformanceHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Course Form & Dialog States
    private val _courseFormState = MutableStateFlow(CourseFormState())
    val courseFormState: StateFlow<CourseFormState> = _courseFormState.asStateFlow()

    private val _isAddEditCourseOpen = MutableStateFlow(false)
    val isAddEditCourseOpen: StateFlow<Boolean> = _isAddEditCourseOpen.asStateFlow()

    private val _courseToDelete = MutableStateFlow<GpaCourseEntity?>(null)
    val courseToDelete: StateFlow<GpaCourseEntity?> = _courseToDelete.asStateFlow()

    // Semester Form & Dialog States
    private val _semesterFormState = MutableStateFlow(SemesterFormState())
    val semesterFormState: StateFlow<SemesterFormState> = _semesterFormState.asStateFlow()

    private val _isAddEditSemesterOpen = MutableStateFlow(false)
    val isAddEditSemesterOpen: StateFlow<Boolean> = _isAddEditSemesterOpen.asStateFlow()

    private val _semesterToDelete = MutableStateFlow<SemesterEntity?>(null)
    val semesterToDelete: StateFlow<SemesterEntity?> = _semesterToDelete.asStateFlow()

    private val _isClearAllDialogOpen = MutableStateFlow(false)
    val isClearAllDialogOpen: StateFlow<Boolean> = _isClearAllDialogOpen.asStateFlow()

    private val _isSemesterSelectorOpen = MutableStateFlow(false)
    val isSemesterSelectorOpen: StateFlow<Boolean> = _isSemesterSelectorOpen.asStateFlow()

    fun selectSemester(semester: SemesterEntity) {
        _selectedSemesterId.value = semester.id
    }

    fun openSemesterSelector() {
        _isSemesterSelectorOpen.value = true
    }

    fun dismissSemesterSelector() {
        _isSemesterSelectorOpen.value = false
    }

    // --- Semester Operations ---

    fun openAddSemesterDialog() {
        val nextNumber = (semesters.value.maxOfOrNull { it.semesterNumber } ?: 0) + 1
        _semesterFormState.value = SemesterFormState(
            name = "Semester $nextNumber",
            academicYear = "2026",
            semesterNumberText = nextNumber.toString(),
            isCurrent = semesters.value.isEmpty()
        )
        _isAddEditSemesterOpen.value = true
    }

    fun openEditSemesterDialog(semester: SemesterEntity) {
        _semesterFormState.value = SemesterFormState(
            id = semester.id,
            name = semester.name,
            academicYear = semester.academicYear,
            semesterNumberText = semester.semesterNumber.toString(),
            startDate = semester.startDate,
            endDate = semester.endDate,
            isCurrent = semester.isCurrent
        )
        _isAddEditSemesterOpen.value = true
    }

    fun dismissAddEditSemesterDialog() {
        _isAddEditSemesterOpen.value = false
        _semesterFormState.value = SemesterFormState()
    }

    fun updateSemesterFormField(transform: SemesterFormState.() -> SemesterFormState) {
        _semesterFormState.update { it.transform().copy(errorMessage = null) }
    }

    fun saveSemester(): Boolean {
        val form = _semesterFormState.value
        if (form.name.isBlank()) {
            _semesterFormState.update { it.copy(errorMessage = "Semester name is required") }
            return false
        }

        val number = form.semesterNumberText.toIntOrNull() ?: 1

        viewModelScope.launch {
            if (form.id == null || form.id == 0L) {
                val newId = semesterRepository.insertSemester(
                    name = form.name,
                    academicYear = form.academicYear,
                    semesterNumber = number,
                    startDate = form.startDate,
                    endDate = form.endDate,
                    isCurrent = form.isCurrent
                )
                _selectedSemesterId.value = newId
            } else {
                semesterRepository.updateSemester(
                    id = form.id,
                    name = form.name,
                    academicYear = form.academicYear,
                    semesterNumber = number,
                    startDate = form.startDate,
                    endDate = form.endDate,
                    isCurrent = form.isCurrent
                )
            }
            dismissAddEditSemesterDialog()
        }
        return true
    }

    fun setSemesterAsCurrent(semester: SemesterEntity) {
        viewModelScope.launch {
            semesterRepository.setCurrentSemester(semester.id)
            _selectedSemesterId.value = semester.id
        }
    }

    fun requestDeleteSemester(semester: SemesterEntity) {
        _semesterToDelete.value = semester
    }

    fun confirmDeleteSemester() {
        val toDelete = _semesterToDelete.value ?: return
        viewModelScope.launch {
            gpaRepository.deleteCoursesBySemesterId(toDelete.id)
            semesterRepository.deleteSemester(toDelete)
            _semesterToDelete.value = null
            val remaining = semesters.value.filter { it.id != toDelete.id }
            _selectedSemesterId.value = remaining.firstOrNull { it.isCurrent }?.id ?: remaining.firstOrNull()?.id
        }
    }

    fun cancelDeleteSemester() {
        _semesterToDelete.value = null
    }

    // --- Course Operations ---

    fun openAddCourseDialog() {
        val semId = selectedSemester.value?.id ?: semesters.value.firstOrNull()?.id ?: 0L
        _courseFormState.value = CourseFormState(semesterId = semId)
        _isAddEditCourseOpen.value = true
    }

    fun openEditCourseDialog(course: GpaCourseEntity) {
        _courseFormState.value = CourseFormState(
            id = course.id,
            semesterId = course.semesterId,
            courseName = course.courseName,
            creditHoursText = course.creditHours.toString(),
            grade = course.grade
        )
        _isAddEditCourseOpen.value = true
    }

    fun dismissAddEditCourseDialog() {
        _isAddEditCourseOpen.value = false
        _courseFormState.value = CourseFormState()
    }

    fun updateCourseFormField(transform: CourseFormState.() -> CourseFormState) {
        _courseFormState.update { it.transform().copy(errorMessage = null) }
    }

    fun saveCourse(): Boolean {
        val form = _courseFormState.value
        val validation = Validators.validateCourseInput(
            courseName = form.courseName,
            creditHoursText = form.creditHoursText
        )

        if (!validation.isValid) {
            _courseFormState.update { it.copy(errorMessage = validation.errorMessage) }
            return false
        }

        val credits = form.creditHoursText.toDouble()
        val currentList = semesters.value
        val fallbackSemId = selectedSemester.value?.id ?: currentList.firstOrNull()?.id ?: 0L
        val targetSemId = if (form.semesterId > 0 && currentList.any { it.id == form.semesterId }) {
            form.semesterId
        } else {
            fallbackSemId
        }

        viewModelScope.launch {
            val finalSemId = if (targetSemId > 0) {
                targetSemId
            } else {
                semesterRepository.ensureDefaultSemesterExists()
            }

            if (form.id == null || form.id == 0L) {
                gpaRepository.insertCourse(
                    semesterId = finalSemId,
                    courseName = form.courseName,
                    creditHours = credits,
                    grade = form.grade
                )
            } else {
                gpaRepository.updateCourse(
                    id = form.id,
                    semesterId = finalSemId,
                    courseName = form.courseName,
                    creditHours = credits,
                    grade = form.grade
                )
            }
            dismissAddEditCourseDialog()
        }
        return true
    }

    fun requestDeleteCourse(course: GpaCourseEntity) {
        _courseToDelete.value = course
    }

    fun confirmDeleteCourse() {
        val toDelete = _courseToDelete.value ?: return
        viewModelScope.launch {
            gpaRepository.deleteCourse(toDelete)
            _courseToDelete.value = null
            if (_isAddEditCourseOpen.value) {
                dismissAddEditCourseDialog()
            }
        }
    }

    fun cancelDeleteCourse() {
        _courseToDelete.value = null
    }

    fun openClearAllDialog() {
        _isClearAllDialogOpen.value = true
    }

    fun dismissClearAllDialog() {
        _isClearAllDialogOpen.value = false
    }

    fun confirmClearAll() {
        viewModelScope.launch {
            val sem = selectedSemester.value
            if (sem != null) {
                gpaRepository.deleteCoursesBySemesterId(sem.id)
            } else {
                gpaRepository.clearAllCourses()
            }
            _isClearAllDialogOpen.value = false
        }
    }
}
