package com.navil.studenthub.ui.screens.exams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.navil.studenthub.theme.AccentGreen
import com.navil.studenthub.theme.AccentGreenContainer
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.theme.PrimaryIndigoContainer
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.ExamWithDetails

@Composable
fun ExamDetailDialog(
    examWithDetails: ExamWithDetails,
    onEdit: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val exam = examWithDetails.exam
    val countdownText = DateUtils.getExamCountdownText(
        dateEpochDay = exam.dateEpochDay,
        startTimeMinutes = exam.startTimeMinutes,
        endTimeMinutes = exam.endTimeMinutes,
        isCompleted = exam.isCompleted
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (exam.isCompleted) AccentGreenContainer else PrimaryIndigoContainer
                    ) {
                        Text(
                            text = countdownText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (exam.isCompleted) AccentGreen else PrimaryIndigo,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onEdit) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit Exam",
                                tint = PrimaryIndigo,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Exam",
                                tint = AccentRose,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Title
                Text(
                    text = exam.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                // Course & Semester
                if (examWithDetails.courseName != null || examWithDetails.semesterName != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (examWithDetails.courseName != null) {
                            DetailItem(
                                icon = Icons.Outlined.Bookmark,
                                label = "Course",
                                value = examWithDetails.courseName,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (examWithDetails.semesterName != null) {
                            DetailItem(
                                icon = Icons.Outlined.Bookmark,
                                label = "Semester",
                                value = examWithDetails.semesterName,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Date & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailItem(
                        icon = Icons.Outlined.Event,
                        label = "Date",
                        value = DateUtils.formatEpochDay(exam.dateEpochDay),
                        modifier = Modifier.weight(1f)
                    )

                    val timeString = if (exam.endTimeMinutes != null) {
                        "${DateUtils.formatTimeMinutes(exam.startTimeMinutes)} — ${DateUtils.formatTimeMinutes(exam.endTimeMinutes)}"
                    } else {
                        DateUtils.formatTimeMinutes(exam.startTimeMinutes)
                    }
                    DetailItem(
                        icon = Icons.Outlined.Schedule,
                        label = "Time",
                        value = timeString,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Room
                if (exam.room.isNotBlank()) {
                    DetailItem(
                        icon = Icons.Outlined.LocationOn,
                        label = "Location",
                        value = exam.room,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Notes
                if (exam.notes.isNotBlank()) {
                    DetailItem(
                        icon = Icons.AutoMirrored.Outlined.Notes,
                        label = "Notes",
                        value = exam.notes,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Toggle Completed Action Button
                Button(
                    onClick = onToggleComplete,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (exam.isCompleted) AccentGreenContainer else PrimaryIndigo
                    )
                ) {
                    Icon(
                        imageVector = if (exam.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                        contentDescription = null,
                        tint = if (exam.isCompleted) AccentGreen else MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (exam.isCompleted) "Completed" else "Mark as Completed",
                        fontWeight = FontWeight.Bold,
                        color = if (exam.isCompleted) AccentGreen else MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryIndigo,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
