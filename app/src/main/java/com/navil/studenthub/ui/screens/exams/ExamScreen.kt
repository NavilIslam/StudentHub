package com.navil.studenthub.ui.screens.exams

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AssignmentLate
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navil.studenthub.model.ExamFilter
import com.navil.studenthub.theme.AccentAmber
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer
import com.navil.studenthub.ui.components.ConfirmDeleteDialog
import com.navil.studenthub.ui.components.EmptyState
import com.navil.studenthub.ui.components.ExamCard
import com.navil.studenthub.ui.components.SectionHeader
import com.navil.studenthub.ui.components.StudentHubTopBar
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.ExamViewModel
import com.navil.studenthub.viewmodel.ExamWithDetails

@Composable
fun ExamScreen(
    viewModel: ExamViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentFilter by viewModel.currentFilter.collectAsState()
    val examCounts by viewModel.examCounts.collectAsState()
    val examsWithDetails by viewModel.filteredExamsWithDetails.collectAsState()
    val nearestUpcomingExam by viewModel.nearestUpcomingExam.collectAsState()
    val availableCourses by viewModel.availableCourses.collectAsState()
    val availableSemesters by viewModel.availableSemesters.collectAsState()

    val isAddEditOpen by viewModel.isAddEditOpen.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val isDetailOpen by viewModel.isDetailOpen.collectAsState()
    val selectedExamForDetail by viewModel.selectedExamForDetail.collectAsState()
    val examToDelete by viewModel.examToDelete.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StudentHubTopBar(
                title = "Exams",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.openAddDialog() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exam",
                            tint = PrimaryIndigo
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Exam", fontWeight = FontWeight.Bold) },
                containerColor = PrimaryIndigo,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 60.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Subtitle
            item {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Prepare ahead. Stay confident.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 1. Next Exam Countdown Hero Card
            nearestUpcomingExam?.let { nextExam ->
                item {
                    NextExamHeroCard(
                        examWithDetails = nextExam,
                        onClick = { viewModel.openDetailDialog(nextExam) }
                    )
                }
            }

            // 2. Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ExamFilter.entries) { filter ->
                        val isSelected = filter == currentFilter
                        val count = when (filter) {
                            ExamFilter.ALL -> examCounts.allCount
                            ExamFilter.UPCOMING -> examCounts.upcomingCount
                            ExamFilter.TODAY -> examCounts.todayCount
                            ExamFilter.COMPLETED -> examCounts.completedCount
                        }

                        val bgColor by animateColorAsState(
                            targetValue = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                            animationSpec = tween(200),
                            label = "exam_filter_bg"
                        )
                        val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        val badgeBg = if (isSelected) Color.White.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant

                        Card(
                            modifier = Modifier.clickable { viewModel.setFilter(filter) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = filter.title,
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
            }

            // 3. Section Header for list
            item {
                SectionHeader(
                    title = "${currentFilter.title.uppercase()} EXAMS (${examsWithDetails.size})"
                )
            }

            // 4. Exam Items List
            if (examsWithDetails.isEmpty()) {
                item {
                    EmptyState(
                        message = "NO EXAMS FOUND",
                        subMessage = if (currentFilter == ExamFilter.ALL) {
                            "Add your exams so Student Hub can help you stay prepared."
                        } else {
                            "No exams matching \"${currentFilter.title}\" filter."
                        },
                        icon = Icons.Outlined.AssignmentLate,
                        buttonText = "Add Exam",
                        onButtonClick = { viewModel.openAddDialog() }
                    )
                }
            } else {
                items(examsWithDetails, key = { "exam_${it.exam.id}" }) { examItem ->
                    ExamCard(
                        examWithDetails = examItem,
                        onClick = { viewModel.openDetailDialog(examItem) },
                        onToggleComplete = { viewModel.toggleExamCompletion(examItem.exam) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }

    // Dialog: Add/Edit Exam
    if (isAddEditOpen) {
        AddEditExamDialog(
            formState = formState,
            courses = availableCourses,
            semesters = availableSemesters,
            onFieldChange = { viewModel.updateFormField(it) },
            onSave = { viewModel.saveExam() },
            onDelete = if (formState.id != null) {
                {
                    val currentExam = examsWithDetails.firstOrNull { it.exam.id == formState.id }
                    if (currentExam != null) {
                        viewModel.requestDeleteExam(currentExam)
                    }
                }
            } else null,
            onDismiss = { viewModel.dismissAddEditDialog() }
        )
    }

    // Dialog: Exam Details
    if (isDetailOpen && selectedExamForDetail != null) {
        ExamDetailDialog(
            examWithDetails = selectedExamForDetail!!,
            onEdit = { viewModel.openEditDialog(selectedExamForDetail!!) },
            onToggleComplete = { viewModel.toggleExamCompletion(selectedExamForDetail!!.exam) },
            onDelete = { viewModel.requestDeleteExam(selectedExamForDetail!!) },
            onDismiss = { viewModel.dismissDetailDialog() }
        )
    }

    // Dialog: Confirm Delete
    if (examToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Exam",
            message = "Are you sure you want to delete \"${examToDelete?.exam?.title}\"?",
            onConfirm = { viewModel.confirmDeleteExam() },
            onDismiss = { viewModel.cancelDeleteExam() }
        )
    }
}

/**
 * Featured Next Exam Hero Card with Countdown
 */
@Composable
private fun NextExamHeroCard(
    examWithDetails: ExamWithDetails,
    onClick: () -> Unit
) {
    val exam = examWithDetails.exam
    val countdownText = DateUtils.getExamCountdownText(
        dateEpochDay = exam.dateEpochDay,
        startTimeMinutes = exam.startTimeMinutes,
        endTimeMinutes = exam.endTimeMinutes,
        isCompleted = exam.isCompleted
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: NEXT EXAM & Big Countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NEXT EXAM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryIndigo,
                    letterSpacing = 1.2.sp
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryIndigoContainer
                ) {
                    Text(
                        text = countdownText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryIndigo,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Title & Course
            Column {
                Text(
                    text = exam.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (examWithDetails.courseName != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = examWithDetails.courseName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryIndigo
                    )
                }
            }

            // Date, Time, Room Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Event,
                        contentDescription = null,
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = DateUtils.formatEpochDay(exam.dateEpochDay),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = DateUtils.formatTimeMinutes(exam.startTimeMinutes),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (exam.room.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = exam.room,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
