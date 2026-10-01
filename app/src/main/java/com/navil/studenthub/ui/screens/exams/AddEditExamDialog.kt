package com.navil.studenthub.ui.screens.exams

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.ExamFormState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExamDialog(
    formState: ExamFormState,
    courses: List<GpaCourseEntity>,
    semesters: List<SemesterEntity>,
    onFieldChange: (ExamFormState.() -> ExamFormState) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var semesterDropdownExpanded by remember { mutableStateOf(false) }

    val selectedDate = LocalDate.ofEpochDay(formState.dateEpochDay)

    val datePickerDialog = remember(formState.dateEpochDay) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                onFieldChange { copy(dateEpochDay = newDate.toEpochDay()) }
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    val startTimePicker = remember(formState.startTimeMinutes) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onFieldChange { copy(startTimeMinutes = hourOfDay * 60 + minute) }
            },
            formState.startTimeMinutes / 60,
            formState.startTimeMinutes % 60,
            false
        )
    }

    val endTimeMinutesVal = formState.endTimeMinutes ?: (formState.startTimeMinutes + 120)
    val endTimePicker = remember(endTimeMinutesVal) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onFieldChange { copy(endTimeMinutes = hourOfDay * 60 + minute) }
            },
            endTimeMinutesVal / 60,
            endTimeMinutesVal % 60,
            false
        )
    }

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
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (formState.id == null) "Add Exam" else "Edit Exam",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Set schedule, course and room details",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (formState.id != null && onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Exam",
                                tint = AccentRose
                            )
                        }
                    }
                }

                // Error Banner
                if (formState.errorMessage != null) {
                    Text(
                        text = formState.errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentRose,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Title
                OutlinedTextField(
                    value = formState.title,
                    onValueChange = { newTitle -> onFieldChange { copy(title = newTitle) } },
                    label = { Text("Exam Title (e.g. Midterm, Final Exam) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Course Dropdown
                val selectedCourseName = courses.firstOrNull { it.id == formState.courseId }?.courseName ?: "No Course Selected"
                ExposedDropdownMenuBox(
                    expanded = courseDropdownExpanded,
                    onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCourseName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Associated Course (Optional)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = courseDropdownExpanded,
                        onDismissRequest = { courseDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                onFieldChange { copy(courseId = null) }
                                courseDropdownExpanded = false
                            }
                        )
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course.courseName) },
                                onClick = {
                                    onFieldChange { copy(courseId = course.id, semesterId = course.semesterId) }
                                    courseDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Semester Dropdown
                val selectedSemName = semesters.firstOrNull { it.id == formState.semesterId }?.name ?: "No Semester Selected"
                ExposedDropdownMenuBox(
                    expanded = semesterDropdownExpanded,
                    onExpandedChange = { semesterDropdownExpanded = !semesterDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSemName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Semester") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = semesterDropdownExpanded,
                        onDismissRequest = { semesterDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                onFieldChange { copy(semesterId = null) }
                                semesterDropdownExpanded = false
                            }
                        )
                        semesters.forEach { sem ->
                            DropdownMenuItem(
                                text = { Text("${sem.name}${if (sem.isCurrent) " (Current)" else ""}") },
                                onClick = {
                                    onFieldChange { copy(semesterId = sem.id) }
                                    semesterDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Date Picker Field
                OutlinedTextField(
                    value = DateUtils.formatEpochDay(formState.dateEpochDay),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Exam Date *") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Event,
                            contentDescription = "Pick Date",
                            tint = PrimaryIndigo,
                            modifier = Modifier.clickable { datePickerDialog.show() }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    shape = RoundedCornerShape(14.dp)
                )

                // Start & End Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = DateUtils.formatTimeMinutes(formState.startTimeMinutes),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Start Time *") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "Pick Start Time",
                                tint = PrimaryIndigo,
                                modifier = Modifier.clickable { startTimePicker.show() }
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { startTimePicker.show() },
                        shape = RoundedCornerShape(14.dp)
                    )

                    val endTimeDisplay = formState.endTimeMinutes?.let { DateUtils.formatTimeMinutes(it) } ?: "Not Set"
                    OutlinedTextField(
                        value = endTimeDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("End Time") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "Pick End Time",
                                tint = PrimaryIndigo,
                                modifier = Modifier.clickable { endTimePicker.show() }
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { endTimePicker.show() },
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                // Room
                OutlinedTextField(
                    value = formState.room,
                    onValueChange = { r -> onFieldChange { copy(room = r) } },
                    label = { Text("Room / Location (e.g. Room 302, Hall A)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Notes
                OutlinedTextField(
                    value = formState.notes,
                    onValueChange = { n -> onFieldChange { copy(notes = n) } },
                    label = { Text("Notes (e.g. Bring calculator & ID)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                    ) {
                        Text("Save Exam", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
