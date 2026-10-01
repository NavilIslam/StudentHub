package com.navil.studenthub.ui.screens.gpa

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer
import com.navil.studenthub.ui.components.ConfirmDeleteDialog
import com.navil.studenthub.ui.components.EmptyState
import com.navil.studenthub.ui.components.GradeBadge
import com.navil.studenthub.ui.components.SectionHeader
import com.navil.studenthub.ui.components.StudentHubTopBar
import com.navil.studenthub.util.GpaCalculator
import com.navil.studenthub.viewmodel.GpaViewModel

@Composable
fun GpaScreen(
    viewModel: GpaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val semesters by viewModel.semesters.collectAsState()
    val selectedSemester by viewModel.selectedSemester.collectAsState()
    val courses by viewModel.coursesForSelectedSemester.collectAsState()
    val selectedSemesterGpa by viewModel.selectedSemesterGpaSummary.collectAsState()
    val cgpaSummary by viewModel.cgpaSummary.collectAsState()
    val semesterHistory by viewModel.semesterHistory.collectAsState()

    val isAddEditCourseOpen by viewModel.isAddEditCourseOpen.collectAsState()
    val courseFormState by viewModel.courseFormState.collectAsState()
    val courseToDelete by viewModel.courseToDelete.collectAsState()

    val isAddEditSemesterOpen by viewModel.isAddEditSemesterOpen.collectAsState()
    val semesterFormState by viewModel.semesterFormState.collectAsState()
    val semesterToDelete by viewModel.semesterToDelete.collectAsState()
    val isClearAllDialogOpen by viewModel.isClearAllDialogOpen.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StudentHubTopBar(
                title = "GPA & CGPA",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.openAddSemesterDialog() }) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = "Add Semester",
                            tint = PrimaryIndigo
                        )
                    }
                    if (courses.isNotEmpty()) {
                        IconButton(onClick = { viewModel.openClearAllDialog() }) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteSweep,
                                contentDescription = "Clear Semester Courses",
                                tint = AccentRose
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (semesters.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openAddCourseDialog() },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Course", fontWeight = FontWeight.Bold) },
                    containerColor = PrimaryIndigo,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 60.dp)
                )
            }
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
            // 1. Overall Academic Performance Summary (CGPA & Highlights)
            item {
                Spacer(modifier = Modifier.height(4.dp))
                AcademicPerformanceHeroCard(
                    cgpa = cgpaSummary.gpa,
                    totalCredits = cgpaSummary.totalCredits,
                    completedCredits = cgpaSummary.completedCredits,
                    totalCourses = cgpaSummary.totalCourses,
                    semesterGpa = selectedSemesterGpa.gpa
                )
            }

            // 2. Horizontal Semester Selector Tabs (Instant 1-tap switching)
            if (semesters.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "SEMESTERS",
                        actionText = "+ Add Semester",
                        onActionClick = { viewModel.openAddSemesterDialog() }
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(semesters, key = { "sem_tab_${it.id}" }) { sem ->
                            val isSelected = sem.id == selectedSemester?.id
                            val containerColor by animateColorAsState(
                                targetValue = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                                animationSpec = tween(200),
                                label = "sem_tab_bg"
                            )
                            val contentColor by animateColorAsState(
                                targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                animationSpec = tween(200),
                                label = "sem_tab_fg"
                            )

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = containerColor,
                                border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)) else null,
                                shadowElevation = if (isSelected) 2.dp else 0.dp,
                                modifier = Modifier.clickable { viewModel.selectSemester(sem) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sem.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = contentColor
                                    )
                                    if (sem.isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(if (isSelected) Color.White else AccentGreen, CircleShape)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    EmptyState(
                        message = "ACADEMIC JOURNEY STARTS HERE",
                        subMessage = "Create your first semester to start tracking your GPA and CGPA.",
                        icon = Icons.Outlined.School,
                        buttonText = "Add Semester",
                        onButtonClick = { viewModel.openAddSemesterDialog() }
                    )
                }
            }

            // 3. Selected Semester Detail Card
            selectedSemester?.let { sem ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = sem.name.uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            letterSpacing = 1.sp
                                        )
                                        if (sem.isCurrent) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = AccentGreenContainer
                                            ) {
                                                Text(
                                                    text = "CURRENT",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                    fontWeight = FontWeight.Bold,
                                                    color = AccentGreen,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = String.format(java.util.Locale.ENGLISH, "%.2f", selectedSemesterGpa.gpa),
                                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryIndigo
                                    )
                                    Text(
                                        text = "Semester GPA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { viewModel.openEditSemesterDialog(sem) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Edit Semester",
                                            tint = PrimaryIndigo,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.requestDeleteSemester(sem) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete Semester",
                                            tint = AccentRose,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${selectedSemesterGpa.totalCredits} Credits • ${courses.size} Courses",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (!sem.isCurrent) {
                                    TextButton(
                                        onClick = { viewModel.setSemesterAsCurrent(sem) }
                                    ) {
                                        Text(
                                            text = "Set as Current",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryIndigo
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Courses in Selected Semester
            item {
                SectionHeader(
                    title = "COURSES IN ${selectedSemester?.name?.uppercase() ?: "SEMESTER"} (${courses.size})",
                    actionText = "+ Add Course",
                    onActionClick = { viewModel.openAddCourseDialog() }
                )
            }

            if (courses.isEmpty() && semesters.isNotEmpty()) {
                item {
                    EmptyState(
                        message = "NO COURSES YET",
                        subMessage = "Add your courses to calculate this semester's GPA.",
                        icon = Icons.Outlined.Calculate,
                        buttonText = "Add Course",
                        onButtonClick = { viewModel.openAddCourseDialog() }
                    )
                }
            } else {
                items(courses, key = { "course_${it.id}" }) { course ->
                    V2CourseCard(
                        course = course,
                        onClick = { viewModel.openEditCourseDialog(course) }
                    )
                }
            }

            // 5. Academic History / Progression
            if (semesterHistory.size > 1) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader(title = "ACADEMIC HISTORY")
                }

                items(semesterHistory, key = { "history_${it.semesterId}" }) { hist ->
                    val isSelected = hist.semesterId == selectedSemester?.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val found = semesters.firstOrNull { it.id == hist.semesterId }
                                if (found != null) viewModel.selectSemester(found)
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PrimaryIndigoContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = hist.semesterName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (hist.isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = AccentGreenContainer
                                        ) {
                                            Text(
                                                text = "CURRENT",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                fontWeight = FontWeight.Bold,
                                                color = AccentGreen,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${hist.totalCredits} Credits • ${hist.totalCourses} Courses",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = String.format(java.util.Locale.ENGLISH, "%.2f", hist.gpa),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryIndigo
                                )
                                Text(
                                    text = "GPA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }

    // Dialog: Add/Edit Course
    if (isAddEditCourseOpen) {
        AddEditCourseDialog(
            formState = courseFormState,
            semesterName = selectedSemester?.name,
            onFieldChange = { viewModel.updateCourseFormField(it) },
            onSave = { viewModel.saveCourse() },
            onDelete = if (courseFormState.id != null) {
                {
                    val currentCourse = courses.firstOrNull { it.id == courseFormState.id }
                    if (currentCourse != null) {
                        viewModel.requestDeleteCourse(currentCourse)
                    }
                }
            } else null,
            onDismiss = { viewModel.dismissAddEditCourseDialog() }
        )
    }

    // Dialog: Add/Edit Semester
    if (isAddEditSemesterOpen) {
        AddEditSemesterDialog(
            formState = semesterFormState,
            onFieldChange = { viewModel.updateSemesterFormField(it) },
            onSave = { viewModel.saveSemester() },
            onDelete = if (semesterFormState.id != null) {
                {
                    val sem = semesters.firstOrNull { it.id == semesterFormState.id }
                    if (sem != null) {
                        viewModel.dismissAddEditSemesterDialog()
                        viewModel.requestDeleteSemester(sem)
                    }
                }
            } else null,
            onDismiss = { viewModel.dismissAddEditSemesterDialog() }
        )
    }

    // Dialog: Confirm Delete Course
    if (courseToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Course",
            message = "Are you sure you want to delete \"${courseToDelete?.courseName}\"?",
            onConfirm = { viewModel.confirmDeleteCourse() },
            onDismiss = { viewModel.cancelDeleteCourse() }
        )
    }

    // Dialog: Confirm Delete Semester
    if (semesterToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Semester",
            message = "Are you sure you want to delete \"${semesterToDelete?.name}\"? All courses inside this semester will also be deleted.",
            onConfirm = { viewModel.confirmDeleteSemester() },
            onDismiss = { viewModel.cancelDeleteSemester() }
        )
    }

    // Dialog: Clear All Semester Courses
    if (isClearAllDialogOpen) {
        ConfirmDeleteDialog(
            title = "Clear Semester Courses",
            message = "Are you sure you want to delete all courses in ${selectedSemester?.name}?",
            onConfirm = { viewModel.confirmClearAll() },
            onDismiss = { viewModel.dismissClearAllDialog() }
        )
    }
}

/**
 * Top Academic Performance Hero Card with CGPA and Semester GPA metrics
 */
@Composable
private fun AcademicPerformanceHeroCard(
    cgpa: Double,
    totalCredits: Double,
    completedCredits: Double,
    totalCourses: Int,
    semesterGpa: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ACADEMIC PERFORMANCE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryIndigo,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = String.format(java.util.Locale.ENGLISH, "%.2f", cgpa),
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 44.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Overall Cumulative CGPA",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Key Metrics: Semester GPA, Total Credits, Completed Credits, Total Courses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PerformanceMetricItem(
                    label = "Semester GPA",
                    value = String.format(java.util.Locale.ENGLISH, "%.2f", semesterGpa),
                    highlightColor = PrimaryIndigo
                )
                PerformanceMetricItem(
                    label = "Total Credits",
                    value = String.format(java.util.Locale.ENGLISH, "%.1f", totalCredits),
                    highlightColor = MaterialTheme.colorScheme.onSurface
                )
                PerformanceMetricItem(
                    label = "Completed",
                    value = String.format(java.util.Locale.ENGLISH, "%.1f", completedCredits),
                    highlightColor = AccentGreen
                )
                PerformanceMetricItem(
                    label = "Courses",
                    value = "$totalCourses",
                    highlightColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PerformanceMetricItem(
    label: String,
    value: String,
    highlightColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = highlightColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun V2CourseCard(
    course: GpaCourseEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${course.creditHours} Credits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    GradeBadge(grade = course.grade)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format(java.util.Locale.ENGLISH, "%.2f pts", course.grade.gradePoint),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
