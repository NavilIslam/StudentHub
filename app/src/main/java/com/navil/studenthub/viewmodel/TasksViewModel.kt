package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navil.studenthub.data.local.entity.TaskEntity
import com.navil.studenthub.data.repository.TaskRepository
import com.navil.studenthub.model.Priority
import com.navil.studenthub.model.TaskFilter
import com.navil.studenthub.model.TaskSortOrder
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskCounts(
    val allCount: Int = 0,
    val todayCount: Int = 0,
    val upcomingCount: Int = 0,
    val completedCount: Int = 0
)

data class TaskFormState(
    val id: Long? = null,
    val title: String = "",
    val course: String = "",
    val description: String = "",
    val dueDateEpochDay: Long = DateUtils.getTodayEpochDay(),
    val dueTimeMinutes: Int? = 17 * 60, // 5:00 PM
    val priority: Priority = Priority.MEDIUM,
    val hasReminder: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

class TasksViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter.ALL)
    val filter: StateFlow<TaskFilter> = _filter.asStateFlow()

    private val _sortOrder = MutableStateFlow(TaskSortOrder.DUE_DATE)
    val sortOrder: StateFlow<TaskSortOrder> = _sortOrder.asStateFlow()

    private val _formState = MutableStateFlow(TaskFormState())
    val formState: StateFlow<TaskFormState> = _formState.asStateFlow()

    private val _isAddEditOpen = MutableStateFlow(false)
    val isAddEditOpen: StateFlow<Boolean> = _isAddEditOpen.asStateFlow()

    private val _selectedTaskForDetail = MutableStateFlow<TaskEntity?>(null)
    val selectedTaskForDetail: StateFlow<TaskEntity?> = _selectedTaskForDetail.asStateFlow()

    private val _taskToDelete = MutableStateFlow<TaskEntity?>(null)
    val taskToDelete: StateFlow<TaskEntity?> = _taskToDelete.asStateFlow()

    private val allTasksFlow = taskRepository.getAllTasks()

    val taskCounts: StateFlow<TaskCounts> = allTasksFlow.combine(_filter) { tasks, _ ->
        val todayEpoch = DateUtils.getTodayEpochDay()
        TaskCounts(
            allCount = tasks.size,
            todayCount = tasks.count { it.dueDateEpochDay == todayEpoch },
            upcomingCount = tasks.count { !it.isCompleted && it.dueDateEpochDay >= todayEpoch },
            completedCount = tasks.count { it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskCounts()
    )

    val filteredTasks: StateFlow<List<TaskEntity>> = combine(
        allTasksFlow,
        _filter,
        _sortOrder
    ) { tasks, filter, sortOrder ->
        val todayEpoch = DateUtils.getTodayEpochDay()

        val filtered = when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.TODAY -> tasks.filter { it.dueDateEpochDay == todayEpoch }
            TaskFilter.UPCOMING -> tasks.filter { !it.isCompleted && it.dueDateEpochDay >= todayEpoch }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }

        when (sortOrder) {
            TaskSortOrder.DUE_DATE -> filtered.sortedWith(
                compareBy<TaskEntity> { it.isCompleted }
                    .thenBy { it.dueDateEpochDay }
                    .thenBy { it.dueTimeMinutes ?: Int.MAX_VALUE }
            )
            TaskSortOrder.PRIORITY -> filtered.sortedWith(
                compareBy<TaskEntity> { it.isCompleted }
                    .thenByDescending { it.priority.level }
                    .thenBy { it.dueDateEpochDay }
            )
            TaskSortOrder.COURSE -> filtered.sortedWith(
                compareBy<TaskEntity> { it.isCompleted }
                    .thenBy { it.course.lowercase() }
                    .thenBy { it.dueDateEpochDay }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val groupedTasks: StateFlow<Map<String, List<TaskEntity>>> = filteredTasks.map { tasks ->
        if (tasks.isEmpty()) emptyMap()
        else {
            val map = linkedMapOf<String, MutableList<TaskEntity>>()
            tasks.forEach { task ->
                val groupKey = if (task.isCompleted) {
                    "COMPLETED"
                } else {
                    DateUtils.getRelativeDueDate(task.dueDateEpochDay).groupKey
                }
                map.getOrPut(groupKey) { mutableListOf() }.add(task)
            }
            map
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    fun setFilter(filter: TaskFilter) {
        _filter.value = filter
    }

    fun setSortOrder(sortOrder: TaskSortOrder) {
        _sortOrder.value = sortOrder
    }

    fun openAddTaskDialog() {
        _formState.value = TaskFormState(dueDateEpochDay = DateUtils.getTodayEpochDay())
        _isAddEditOpen.value = true
    }

    fun openEditTaskDialog(task: TaskEntity) {
        _formState.value = TaskFormState(
            id = task.id,
            title = task.title,
            course = task.course,
            description = task.description,
            dueDateEpochDay = task.dueDateEpochDay,
            dueTimeMinutes = task.dueTimeMinutes,
            priority = task.priority,
            hasReminder = task.hasReminder,
            isCompleted = task.isCompleted
        )
        _isAddEditOpen.value = true
    }

    fun openTaskDetail(task: TaskEntity) {
        _selectedTaskForDetail.value = task
    }

    fun dismissTaskDetail() {
        _selectedTaskForDetail.value = null
    }

    fun dismissAddEditDialog() {
        _isAddEditOpen.value = false
        _formState.value = TaskFormState()
    }

    fun updateFormField(transform: TaskFormState.() -> TaskFormState) {
        _formState.update { it.transform().copy(errorMessage = null) }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            val updatedState = !task.isCompleted
            taskRepository.updateTaskCompletion(task.id, updatedState)
            if (_selectedTaskForDetail.value?.id == task.id) {
                _selectedTaskForDetail.value = task.copy(isCompleted = updatedState)
            }
        }
    }

    fun saveTask(): Boolean {
        val form = _formState.value
        val validation = Validators.validateTaskInput(
            title = form.title,
            dueDateEpochDay = form.dueDateEpochDay
        )

        if (!validation.isValid) {
            _formState.update { it.copy(errorMessage = validation.errorMessage) }
            return false
        }

        viewModelScope.launch {
            val entity = TaskEntity(
                id = form.id ?: 0L,
                title = form.title.trim(),
                course = form.course.trim(),
                description = form.description.trim(),
                dueDateEpochDay = form.dueDateEpochDay,
                dueTimeMinutes = form.dueTimeMinutes,
                priority = form.priority,
                hasReminder = form.hasReminder,
                isCompleted = form.isCompleted
            )

            if (form.id == null || form.id == 0L) {
                taskRepository.insertTask(entity)
            } else {
                taskRepository.updateTask(entity)
                if (_selectedTaskForDetail.value?.id == entity.id) {
                    _selectedTaskForDetail.value = entity
                }
            }
            dismissAddEditDialog()
        }
        return true
    }

    fun requestDeleteTask(task: TaskEntity) {
        _taskToDelete.value = task
    }

    fun confirmDeleteTask() {
        val toDelete = _taskToDelete.value ?: return
        viewModelScope.launch {
            taskRepository.deleteTask(toDelete)
            _taskToDelete.value = null
            if (_selectedTaskForDetail.value?.id == toDelete.id) {
                _selectedTaskForDetail.value = null
            }
            if (_isAddEditOpen.value) {
                dismissAddEditDialog()
            }
        }
    }

    fun cancelDelete() {
        _taskToDelete.value = null
    }
}
