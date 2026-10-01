package com.navil.studenthub.ui.screens.tasks

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navil.studenthub.data.local.entity.TaskEntity
import com.navil.studenthub.model.TaskFilter
import com.navil.studenthub.model.TaskSortOrder
import com.navil.studenthub.theme.AccentAmber
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.AccentRoseContainer
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer
import com.navil.studenthub.ui.components.ConfirmDeleteDialog
import com.navil.studenthub.ui.components.EmptyState
import com.navil.studenthub.ui.components.PriorityBadge
import com.navil.studenthub.ui.components.StudentHubTopBar
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.TasksViewModel

@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.filteredTasks.collectAsState()
    val groupedTasks by viewModel.groupedTasks.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val taskCounts by viewModel.taskCounts.collectAsState()
    val isAddEditOpen by viewModel.isAddEditOpen.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val selectedTaskForDetail by viewModel.selectedTaskForDetail.collectAsState()
    val taskToDelete by viewModel.taskToDelete.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StudentHubTopBar(
                title = "Tasks",
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort Tasks")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            TaskSortOrder.entries.forEach { order ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Sort by ${order.title}",
                                            fontWeight = if (order == sortOrder) FontWeight.Bold else FontWeight.Normal,
                                            color = if (order == sortOrder) PrimaryIndigo else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.setSortOrder(order)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddTaskDialog() },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Task", fontWeight = FontWeight.Bold) },
                containerColor = PrimaryIndigo,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 60.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Info: "Stay on top of your work." + TODAY count pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stay on top of your work.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryIndigoContainer
                ) {
                    Text(
                        text = "TODAY • ${taskCounts.todayCount} tasks",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryIndigo,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips (All, Today, Upcoming, Completed)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TaskFilter.entries) { item ->
                    val isSelected = item == filter
                    val count = when (item) {
                        TaskFilter.ALL -> taskCounts.allCount
                        TaskFilter.TODAY -> taskCounts.todayCount
                        TaskFilter.UPCOMING -> taskCounts.upcomingCount
                        TaskFilter.COMPLETED -> taskCounts.completedCount
                    }

                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                        animationSpec = tween(200),
                        label = "filter_bg"
                    )
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    val badgeBg = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant

                    Card(
                        modifier = Modifier.clickable { viewModel.setFilter(item) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(badgeBg)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Task List (Grouped by Date)
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = "ALL CAUGHT UP 🎉",
                        subMessage = "No tasks found in this section.",
                        icon = Icons.Outlined.CheckCircle,
                        buttonText = "Add Task",
                        onButtonClick = { viewModel.openAddTaskDialog() }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    groupedTasks.forEach { (groupTitle, groupTasks) ->
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = groupTitle,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                        }

                        items(groupTasks, key = { "task_${it.id}" }) { task ->
                            V2TaskCard(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                                onClick = { viewModel.openTaskDetail(task) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(110.dp))
                    }
                }
            }
        }
    }

    if (isAddEditOpen) {
        AddEditTaskDialog(
            formState = formState,
            onFieldChange = { viewModel.updateFormField(it) },
            onSave = { viewModel.saveTask() },
            onDelete = if (formState.id != null) {
                {
                    val currentTask = tasks.firstOrNull { it.id == formState.id }
                    if (currentTask != null) {
                        viewModel.requestDeleteTask(currentTask)
                    }
                }
            } else null,
            onDismiss = { viewModel.dismissAddEditDialog() }
        )
    }

    selectedTaskForDetail?.let { task ->
        TaskDetailDialog(
            task = task,
            onToggleComplete = { viewModel.toggleTaskCompletion(task) },
            onEdit = {
                viewModel.dismissTaskDetail()
                viewModel.openEditTaskDialog(task)
            },
            onDelete = {
                viewModel.requestDeleteTask(task)
            },
            onDismiss = { viewModel.dismissTaskDetail() }
        )
    }

    if (taskToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Task",
            message = "Are you sure you want to delete \"${taskToDelete?.title}\"? This cannot be undone.",
            onConfirm = { viewModel.confirmDeleteTask() },
            onDismiss = { viewModel.cancelDelete() }
        )
    }
}

/**
 * Compact V2 Task Card with Animated Checkbox, Center Title+Course, Right Priority+Date
 */
@Composable
private fun V2TaskCard(
    task: TaskEntity,
    onToggleComplete: () -> Unit,
    onClick: () -> Unit
) {
    val relativeDate = DateUtils.getRelativeDueDate(task.dueDateEpochDay)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (relativeDate.isOverdue && !task.isCompleted) AccentRose.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Interactive Animated Checkbox
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) AccentGreen
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    )
                    .border(
                        1.5.dp,
                        if (task.isCompleted) AccentGreen else MaterialTheme.colorScheme.outlineVariant,
                        CircleShape
                    )
                    .clickable(onClick = onToggleComplete),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center: Title + Course
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.course.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.course,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else PrimaryIndigo,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Relative Date text
                Text(
                    text = relativeDate.text,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    fontWeight = if (relativeDate.isUrgent) FontWeight.Bold else FontWeight.Normal,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else if (relativeDate.isOverdue) AccentRose
                    else if (relativeDate.isUrgent) AccentAmber
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Priority Badge
            PriorityBadge(priority = task.priority)
        }
    }
}
