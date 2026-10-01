package com.navil.studenthub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.AssignmentLate
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navil.studenthub.model.AcademicCalendarEvent
import com.navil.studenthub.model.CalendarEventType
import com.navil.studenthub.theme.AccentAmber
import com.navil.studenthub.theme.AccentAmberContainer
import com.navil.studenthub.theme.AccentCyan
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.AccentRoseContainer
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer
import com.navil.studenthub.util.DateUtils

@Composable
fun CalendarEventCard(
    event: AcademicCalendarEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (typeColor, typeBgColor, typeIcon, typeLabel) = when (event.type) {
        CalendarEventType.CLASS -> Quadruple(PrimaryIndigo, PrimaryIndigoContainer, Icons.Outlined.School, "CLASS")
        CalendarEventType.TASK -> Quadruple(AccentAmber, AccentAmberContainer, Icons.Outlined.CheckCircle, "TASK")
        CalendarEventType.EXAM -> Quadruple(AccentRose, AccentRoseContainer, Icons.Outlined.AssignmentLate, "EXAM")
    }

    val isClassActive = if (event is AcademicCalendarEvent.ClassEvent) {
        DateUtils.isClassCurrentlyActive(
            dayOfWeek = event.classEntity.dayOfWeek,
            startTimeMinutes = event.classEntity.startTimeMinutes,
            endTimeMinutes = event.classEntity.endTimeMinutes
        )
    } else false

    val cardAlpha = if (event.isCompleted) 0.65f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = cardAlpha)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (event.isCompleted) 0.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isClassActive) AccentGreen.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left color stripe
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxWidth()
                    .background(if (isClassActive) AccentGreen else typeColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header row: Type badge, Course Tag, and Status indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        // Type Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = typeBgColor
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = typeIcon,
                                    contentDescription = null,
                                    tint = typeColor,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = typeLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = typeColor
                                )
                            }
                        }

                        // Course Tag (if present)
                        if (event.courseName != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                            ) {
                                Text(
                                    text = event.courseName!!,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    // Status / Countdown Badge
                    when (event) {
                        is AcademicCalendarEvent.ExamEvent -> {
                            val countdown = DateUtils.getExamCountdownText(
                                dateEpochDay = event.examEntity.dateEpochDay,
                                startTimeMinutes = event.examEntity.startTimeMinutes,
                                endTimeMinutes = event.examEntity.endTimeMinutes,
                                isCompleted = event.examEntity.isCompleted
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (event.isCompleted) AccentGreenContainer else AccentRoseContainer
                            ) {
                                Text(
                                    text = countdown,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (event.isCompleted) AccentGreen else AccentRose,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                        is AcademicCalendarEvent.TaskEvent -> {
                            if (event.isCompleted) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AccentGreenContainer
                                ) {
                                    Text(
                                        text = "✓ Completed",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGreen,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                PriorityBadge(priority = event.priority)
                            }
                        }
                        is AcademicCalendarEvent.ClassEvent -> {
                            if (isClassActive) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AccentGreenContainer
                                ) {
                                    Text(
                                        text = "NOW",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (event.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Detail Items Row: Time, Room, Instructor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time
                    val timeString = when (event) {
                        is AcademicCalendarEvent.ClassEvent -> {
                            "${DateUtils.formatTimeMinutes(event.classEntity.startTimeMinutes)} — ${DateUtils.formatTimeMinutes(event.classEntity.endTimeMinutes)}"
                        }
                        is AcademicCalendarEvent.ExamEvent -> {
                            if (event.examEntity.endTimeMinutes != null) {
                                "${DateUtils.formatTimeMinutes(event.examEntity.startTimeMinutes)} — ${DateUtils.formatTimeMinutes(event.examEntity.endTimeMinutes)}"
                            } else {
                                DateUtils.formatTimeMinutes(event.examEntity.startTimeMinutes)
                            }
                        }
                        is AcademicCalendarEvent.TaskEvent -> {
                            if (event.taskEntity.dueTimeMinutes != null) {
                                "Due ${DateUtils.formatTimeMinutes(event.taskEntity.dueTimeMinutes)}"
                            } else {
                                "Due today"
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = typeColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timeString,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Room (for Class or Exam)
                    val roomString = when (event) {
                        is AcademicCalendarEvent.ClassEvent -> event.room
                        is AcademicCalendarEvent.ExamEvent -> event.room
                        is AcademicCalendarEvent.TaskEvent -> ""
                    }

                    if (roomString.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = roomString,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Instructor (for Class)
                    if (event is AcademicCalendarEvent.ClassEvent && event.instructor.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = event.instructor,
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
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
