package com.navil.studenthub.ui.screens.schedule

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AssignmentLate
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.theme.AccentAmber
import com.navil.studenthub.theme.AccentAmberContainer
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.ui.components.ConfirmDeleteDialog
import com.navil.studenthub.ui.components.EmptyState
import com.navil.studenthub.ui.components.StudentHubTopBar
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onNavigateToExams: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedDay by viewModel.selectedDay.collectAsState()
    val classes by viewModel.classesForSelectedDay.collectAsState()
    val examsOnSelectedDay by viewModel.examsForSelectedDay.collectAsState()
    val weekOffset by viewModel.weekOffset.collectAsState()
    val weekHeaderTitle by viewModel.weekHeaderTitle.collectAsState()
    val dayBubbles by viewModel.dayBubbles.collectAsState()

    val isAddEditOpen by viewModel.isAddEditOpen.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val classToDelete by viewModel.classToDelete.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StudentHubTopBar(
                title = "Schedule",
                actions = {
                    if (weekOffset != 0L) {
                        TextButton(onClick = { viewModel.resetToCurrentWeek() }) {
                            Text(
                                text = "Current Week",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryIndigo
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Class", fontWeight = FontWeight.Bold) },
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
            // Week Pagination Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.previousWeek() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Week",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = weekHeaderTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = { viewModel.nextWeek() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Week",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 7 Compact Day Bubbles (SUN, MON, TUE, WED, THU, FRI, SAT)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayBubbles.forEach { bubble ->
                    val isSelected = bubble.dayOfWeek == selectedDay
                    val isToday = bubble.isToday

                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                        animationSpec = tween(200),
                        label = "day_bubble_bg"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        animationSpec = tween(200),
                        label = "day_bubble_fg"
                    )

                    val subTextColor = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp)
                            .clickable { viewModel.selectDay(bubble.dayOfWeek) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = bubble.dayOfWeek.shortName.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontWeight = FontWeight.Bold,
                                color = subTextColor
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${bubble.dayNumber}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )

                            if (isToday) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else PrimaryIndigo, CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timetable & Exam View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // If there are exams on this selected day, show a banner
                if (examsOnSelectedDay.isNotEmpty()) {
                    items(examsOnSelectedDay, key = { "sched_exam_${it.id}" }) { exam ->
                        ExamDayBanner(
                            exam = exam,
                            onClick = onNavigateToExams
                        )
                    }
                }

                if (classes.isEmpty() && examsOnSelectedDay.isEmpty()) {
                    item {
                        EmptyState(
                            message = "YOUR DAY IS CLEAR",
                            subMessage = "No classes scheduled for ${selectedDay.displayName}.",
                            icon = Icons.Outlined.CalendarToday,
                            buttonText = "Add Class",
                            onButtonClick = { viewModel.openAddDialog() }
                        )
                    }
                } else {
                    items(classes, key = { "class_${it.id}" }) { classEntity ->
                        TimetableClassItem(
                            classEntity = classEntity,
                            onClick = { viewModel.openEditDialog(classEntity) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(110.dp))
                }
            }
        }
    }

    if (isAddEditOpen) {
        AddEditClassDialog(
            formState = formState,
            onFieldChange = { viewModel.updateFormField(it) },
            onSave = { viewModel.saveClass() },
            onDelete = if (formState.id != null) {
                {
                    val currentClass = classes.firstOrNull { it.id == formState.id }
                    if (currentClass != null) {
                        viewModel.requestDeleteClass(currentClass)
                    }
                }
            } else null,
            onDismiss = { viewModel.dismissAddEditDialog() }
        )
    }

    if (classToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Class",
            message = "Are you sure you want to delete \"${classToDelete?.subject}\"?",
            onConfirm = { viewModel.confirmDeleteClass() },
            onDismiss = { viewModel.cancelDelete() }
        )
    }
}

/**
 * Visual Banner for Exams on the selected schedule day
 */
@Composable
private fun ExamDayBanner(
    exam: ExamEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AccentAmberContainer.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AccentAmberContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AssignmentLate,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "EXAM TODAY",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = AccentAmber
                        )
                    }
                    Text(
                        text = exam.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${DateUtils.formatTimeMinutes(exam.startTimeMinutes)}${if (exam.room.isNotBlank()) " · ${exam.room}" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "View →",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = AccentAmber
            )
        }
    }
}

/**
 * Vertical Timetable Class Card with left color stripe
 */
@Composable
private fun TimetableClassItem(
    classEntity: ClassEntity,
    onClick: () -> Unit
) {
    val isHappeningNow = DateUtils.isClassCurrentlyActive(
        dayOfWeek = classEntity.dayOfWeek,
        startTimeMinutes = classEntity.startTimeMinutes,
        endTimeMinutes = classEntity.endTimeMinutes
    )

    val classColor = try {
        Color(android.graphics.Color.parseColor(classEntity.colorHex))
    } catch (e: Exception) {
        PrimaryIndigo
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        // Left Column: Time Stem
        Column(
            modifier = Modifier.width(62.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = DateUtils.formatTimeMinutes(classEntity.startTimeMinutes),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = DateUtils.formatTimeMinutes(classEntity.endTimeMinutes),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right Column: Class Card with Left Stripe
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isHappeningNow) AccentGreen.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Color Stripe
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxWidth()
                        .background(classColor)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = classEntity.subject,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (isHappeningNow) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AccentGreenContainer
                            ) {
                                Text(
                                    text = "NOW",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Room & Instructor
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (classEntity.room.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = classEntity.room,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (classEntity.instructor.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = classEntity.instructor,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
