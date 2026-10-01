package com.navil.studenthub.ui.screens.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navil.studenthub.model.AcademicCalendarEvent
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.model.CalendarEventType
import com.navil.studenthub.model.CalendarEventTypeFilter
import com.navil.studenthub.theme.AccentAmber
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.ui.components.CalendarEventCard
import com.navil.studenthub.ui.components.EmptyState
import com.navil.studenthub.ui.components.SectionHeader
import com.navil.studenthub.ui.components.StudentHubTopBar
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.CalendarViewModel
import com.navil.studenthub.viewmodel.ExamViewModel
import com.navil.studenthub.viewmodel.ExamWithDetails
import com.navil.studenthub.viewmodel.ScheduleViewModel
import com.navil.studenthub.viewmodel.TasksViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun AcademicCalendarScreen(
    viewModel: CalendarViewModel,
    scheduleViewModel: ScheduleViewModel,
    tasksViewModel: TasksViewModel,
    examViewModel: ExamViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var semesterMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StudentHubTopBar(
                title = "Academic Calendar",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.jumpToToday() }) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Subtitle
            item {
                Text(
                    text = "Everything important, in one place.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 1. Month Navigation Header (‹ Month Year ›)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.previousMonth() },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = "Previous Month",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                            Text(
                                text = DateUtils.formatMonthYear(uiState.displayedMonth),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            IconButton(
                                onClick = { viewModel.nextMonth() },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "Next Month",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2. Day-of-week headers (SUN, MON, TUE, WED, THU, FRI, SAT)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            AcademicDayOfWeek.entries.forEach { day ->
                                Text(
                                    text = day.shortName.uppercase(),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (day == AcademicDayOfWeek.SUNDAY || day == AcademicDayOfWeek.FRIDAY) PrimaryIndigo else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. Calendar Month Grid
                        CalendarMonthGrid(
                            displayedMonth = uiState.displayedMonth,
                            selectedDate = uiState.selectedDate,
                            monthEventDotsMap = uiState.monthEventDotsMap,
                            onDateSelected = { date -> viewModel.selectDate(date) }
                        )
                    }
                }
            }

            // 4. Selected Date Header & Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = DateUtils.formatCalendarSelectedDate(uiState.selectedDate).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Events & Schedule (${uiState.eventsForSelectedDate.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Optional Semester Filter Menu
                    if (uiState.availableSemesters.isNotEmpty()) {
                        Box {
                            val selectedSemName = uiState.availableSemesters.firstOrNull { it.id == uiState.selectedSemesterId }?.name ?: "All Semesters"
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.clickable { semesterMenuExpanded = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FilterList,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = selectedSemName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = semesterMenuExpanded,
                                onDismissRequest = { semesterMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All Semesters") },
                                    onClick = {
                                        viewModel.setSemesterFilter(null)
                                        semesterMenuExpanded = false
                                    }
                                )
                                uiState.availableSemesters.forEach { sem ->
                                    DropdownMenuItem(
                                        text = { Text(sem.name) },
                                        onClick = {
                                            viewModel.setSemesterFilter(sem.id)
                                            semesterMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Event Type Filter Chips: All, Classes, Tasks, Exams
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(CalendarEventTypeFilter.entries) { filter ->
                        val isSelected = filter == uiState.selectedEventType
                        val count = when (filter) {
                            CalendarEventTypeFilter.ALL -> uiState.totalEventsCount
                            CalendarEventTypeFilter.CLASSES -> uiState.classesCount
                            CalendarEventTypeFilter.TASKS -> uiState.tasksCount
                            CalendarEventTypeFilter.EXAMS -> uiState.examsCount
                        }

                        val bgColor by animateColorAsState(
                            targetValue = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                            animationSpec = tween(200),
                            label = "cal_filter_bg"
                        )
                        val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        val badgeBg = if (isSelected) Color.White.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant

                        Card(
                            modifier = Modifier.clickable { viewModel.setEventTypeFilter(filter) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
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

            // 6. Daily Events List
            if (uiState.eventsForSelectedDate.isEmpty()) {
                item {
                    EmptyState(
                        message = "CLEAR DAY",
                        subMessage = "No classes, tasks or exams scheduled for this date.",
                        icon = Icons.Outlined.CheckCircle,
                        buttonText = "View Schedule",
                        onButtonClick = { viewModel.jumpToToday() }
                    )
                }
            } else {
                items(uiState.eventsForSelectedDate, key = { "cal_${it.id}" }) { event ->
                    CalendarEventCard(
                        event = event,
                        onClick = {
                            when (event) {
                                is AcademicCalendarEvent.ClassEvent -> {
                                    scheduleViewModel.openEditDialog(event.classEntity)
                                }
                                is AcademicCalendarEvent.TaskEvent -> {
                                    tasksViewModel.openTaskDetail(event.taskEntity)
                                }
                                is AcademicCalendarEvent.ExamEvent -> {
                                    examViewModel.openDetailDialog(
                                        ExamWithDetails(
                                            exam = event.examEntity,
                                            courseName = event.courseName,
                                            semesterName = event.semesterName
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * Monthly Calendar Grid rendered dynamically for any YearMonth
 */
@Composable
private fun CalendarMonthGrid(
    displayedMonth: YearMonth,
    selectedDate: LocalDate,
    monthEventDotsMap: Map<LocalDate, List<CalendarEventType>>,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val firstDayOfMonth = displayedMonth.atDay(1)
    val daysInMonth = displayedMonth.lengthOfMonth()

    // Week starts on Sunday
    val startAcademicDay = AcademicDayOfWeek.fromJavaDayOfWeek(firstDayOfMonth.dayOfWeek)
    val leadingEmptyCells = startAcademicDay.dayIndex

    val totalCells = leadingEmptyCells + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (rowIndex in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (colIndex in 0..6) {
                    val cellIndex = rowIndex * 7 + colIndex
                    val dayNumber = cellIndex - leadingEmptyCells + 1

                    if (dayNumber in 1..daysInMonth) {
                        val currentDate = displayedMonth.atDay(dayNumber)
                        val isSelected = currentDate == selectedDate
                        val isToday = currentDate == today
                        val eventDots = monthEventDotsMap[currentDate] ?: emptyList()

                        CalendarDayCell(
                            dayNumber = dayNumber,
                            date = currentDate,
                            isSelected = isSelected,
                            isToday = isToday,
                            eventDots = eventDots,
                            onClick = { onDateSelected(currentDate) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        // Empty placeholder cell
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact, modern Calendar Day Cell with accessibility content description
 */
@Composable
private fun CalendarDayCell(
    dayNumber: Int,
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    eventDots: List<CalendarEventType>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cellDescription = buildString {
        append(DateUtils.formatCalendarSelectedDate(date))
        if (eventDots.isNotEmpty()) {
            append(", ${eventDots.size} event types scheduled")
        } else {
            append(", no events")
        }
    }

    val cellBgColor by animateColorAsState(
        targetValue = when {
            isSelected -> PrimaryIndigo
            isToday -> PrimaryIndigo.copy(alpha = 0.12f)
            else -> Color.Transparent
        },
        animationSpec = tween(180),
        label = "cell_bg"
    )

    val textColor = when {
        isSelected -> Color.White
        isToday -> PrimaryIndigo
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cellBgColor)
            .clickable(onClick = onClick)
            .semantics { contentDescription = cellDescription },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$dayNumber",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Event Dots (up to 3 distinct type dots)
            if (eventDots.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    eventDots.take(3).forEach { type ->
                        val dotColor = if (isSelected) {
                            Color.White.copy(alpha = 0.9f)
                        } else {
                            when (type) {
                                CalendarEventType.CLASS -> PrimaryIndigo
                                CalendarEventType.TASK -> AccentAmber
                                CalendarEventType.EXAM -> AccentRose
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
