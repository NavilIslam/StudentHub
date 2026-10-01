package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.repository.ClassRepository
import com.navil.studenthub.data.repository.ExamRepository
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.util.Validators
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClassFormState(
    val id: Long? = null,
    val subject: String = "",
    val instructor: String = "",
    val dayOfWeek: AcademicDayOfWeek = AcademicDayOfWeek.SUNDAY,
    val startTimeMinutes: Int = 9 * 60,
    val endTimeMinutes: Int = 10 * 60 + 30,
    val room: String = "",
    val notes: String = "",
    val colorHex: String = "#4F46E5",
    val errorMessage: String? = null
)

class ScheduleViewModel(
    private val classRepository: ClassRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _selectedDay = MutableStateFlow(DateUtils.getTodayAcademicDay())
    val selectedDay: StateFlow<AcademicDayOfWeek> = _selectedDay.asStateFlow()

    private val _weekOffset = MutableStateFlow(0L)
    val weekOffset: StateFlow<Long> = _weekOffset.asStateFlow()

    val weekHeaderTitle: StateFlow<String> = _weekOffset.map { offset ->
        DateUtils.getWeekHeaderTitle(offset)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DateUtils.getWeekHeaderTitle(0)
    )

    val dayBubbles: StateFlow<List<DateUtils.DayBubbleInfo>> = _weekOffset.map { offset ->
        DateUtils.getDaysForWeek(offset)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DateUtils.getDaysForWeek(0)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val classesForSelectedDay: StateFlow<List<ClassEntity>> = _selectedDay
        .flatMapLatest { day -> classRepository.getClassesForDay(day) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val examsForSelectedDay: StateFlow<List<ExamEntity>> = combine(
        dayBubbles,
        _selectedDay,
        examRepository.getAllExams()
    ) { bubbles, selected, allExams ->
        val bubble = bubbles.firstOrNull { it.dayOfWeek == selected }
        if (bubble != null) {
            val epochDay = bubble.localDate.toEpochDay()
            allExams.filter { it.dateEpochDay == epochDay }
        } else {
            emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _formState = MutableStateFlow(ClassFormState(dayOfWeek = _selectedDay.value))
    val formState: StateFlow<ClassFormState> = _formState.asStateFlow()

    private val _isAddEditOpen = MutableStateFlow(false)
    val isAddEditOpen: StateFlow<Boolean> = _isAddEditOpen.asStateFlow()

    private val _classToDelete = MutableStateFlow<ClassEntity?>(null)
    val classToDelete: StateFlow<ClassEntity?> = _classToDelete.asStateFlow()

    fun selectDay(day: AcademicDayOfWeek) {
        _selectedDay.value = day
    }

    fun previousWeek() {
        _weekOffset.update { it - 1 }
    }

    fun nextWeek() {
        _weekOffset.update { it + 1 }
    }

    fun resetToCurrentWeek() {
        _weekOffset.value = 0L
        _selectedDay.value = DateUtils.getTodayAcademicDay()
    }

    fun openAddDialog() {
        _formState.value = ClassFormState(dayOfWeek = _selectedDay.value)
        _isAddEditOpen.value = true
    }

    fun openEditDialog(classEntity: ClassEntity) {
        _formState.value = ClassFormState(
            id = classEntity.id,
            subject = classEntity.subject,
            instructor = classEntity.instructor,
            dayOfWeek = classEntity.dayOfWeek,
            startTimeMinutes = classEntity.startTimeMinutes,
            endTimeMinutes = classEntity.endTimeMinutes,
            room = classEntity.room,
            notes = classEntity.notes,
            colorHex = classEntity.colorHex
        )
        _isAddEditOpen.value = true
    }

    fun dismissAddEditDialog() {
        _isAddEditOpen.value = false
        _formState.value = ClassFormState()
    }

    fun updateFormField(transform: ClassFormState.() -> ClassFormState) {
        _formState.update { it.transform().copy(errorMessage = null) }
    }

    fun saveClass(): Boolean {
        val form = _formState.value
        val validation = Validators.validateClassInput(
            subject = form.subject,
            dayOfWeek = form.dayOfWeek,
            startTimeMinutes = form.startTimeMinutes,
            endTimeMinutes = form.endTimeMinutes
        )

        if (!validation.isValid) {
            _formState.update { it.copy(errorMessage = validation.errorMessage) }
            return false
        }

        viewModelScope.launch {
            val entity = ClassEntity(
                id = form.id ?: 0L,
                subject = form.subject.trim(),
                instructor = form.instructor.trim(),
                dayOfWeek = form.dayOfWeek,
                startTimeMinutes = form.startTimeMinutes,
                endTimeMinutes = form.endTimeMinutes,
                room = form.room.trim(),
                notes = form.notes.trim(),
                colorHex = form.colorHex
            )

            if (form.id == null || form.id == 0L) {
                classRepository.insertClass(entity)
            } else {
                classRepository.updateClass(entity)
            }
            dismissAddEditDialog()
        }
        return true
    }

    fun requestDeleteClass(classEntity: ClassEntity) {
        _classToDelete.value = classEntity
    }

    fun confirmDeleteClass() {
        val toDelete = _classToDelete.value ?: return
        viewModelScope.launch {
            classRepository.deleteClass(toDelete)
            _classToDelete.value = null
            if (_isAddEditOpen.value) {
                dismissAddEditDialog()
            }
        }
    }

    fun cancelDelete() {
        _classToDelete.value = null
    }
}
