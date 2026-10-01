package com.navil.studenthub.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.ClassColorPalette
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.util.DateUtils
import com.navil.studenthub.viewmodel.ClassFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClassDialog(
    formState: ClassFormState,
    onFieldChange: (ClassFormState.() -> ClassFormState) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var dayDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with title and descriptive subtitle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (formState.id == null) "Add Class" else "Edit Class",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Schedule lecture and location details",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (formState.id != null && onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Class",
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

                // Subject
                OutlinedTextField(
                    value = formState.subject,
                    onValueChange = { newSubject -> onFieldChange { copy(subject = newSubject) } },
                    label = { Text("Subject / Course Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Instructor
                OutlinedTextField(
                    value = formState.instructor,
                    onValueChange = { newInstructor -> onFieldChange { copy(instructor = newInstructor) } },
                    label = { Text("Instructor (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Day Selection Dropdown
                ExposedDropdownMenuBox(
                    expanded = dayDropdownExpanded,
                    onExpandedChange = { dayDropdownExpanded = !dayDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = formState.dayOfWeek.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Day of Week *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = dayDropdownExpanded,
                        onDismissRequest = { dayDropdownExpanded = false }
                    ) {
                        AcademicDayOfWeek.entries.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day.displayName) },
                                onClick = {
                                    onFieldChange { copy(dayOfWeek = day) }
                                    dayDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Time Pickers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Start Time
                    OutlinedTextField(
                        value = DateUtils.formatTimeMinutes(formState.startTimeMinutes),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Start Time *") },
                        trailingIcon = {
                            IconButton(onClick = { showStartTimePicker = true }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "Pick Start Time")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showStartTimePicker = true },
                        shape = RoundedCornerShape(14.dp)
                    )

                    // End Time
                    OutlinedTextField(
                        value = DateUtils.formatTimeMinutes(formState.endTimeMinutes),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("End Time *") },
                        trailingIcon = {
                            IconButton(onClick = { showEndTimePicker = true }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "Pick End Time")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showEndTimePicker = true },
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                // Room
                OutlinedTextField(
                    value = formState.room,
                    onValueChange = { newRoom -> onFieldChange { copy(room = newRoom) } },
                    label = { Text("Room / Location (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Notes
                OutlinedTextField(
                    value = formState.notes,
                    onValueChange = { newNotes -> onFieldChange { copy(notes = newNotes) } },
                    label = { Text("Notes (Optional)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Accent Color Picker
                Text(
                    text = "Accent Color",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ClassColorPalette.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSelected = formState.colorHex.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { onFieldChange { copy(colorHex = hex) } }
                                .then(
                                    if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    else Modifier
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Action Buttons
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
                        Text("Save Class", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Material 3 Time Pickers
    if (showStartTimePicker) {
        val initialHour = formState.startTimeMinutes / 60
        val initialMinute = formState.startTimeMinutes % 60
        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            title = { Text("Select Start Time", fontWeight = FontWeight.Bold) },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val minutes = timePickerState.hour * 60 + timePickerState.minute
                        onFieldChange { copy(startTimeMinutes = minutes) }
                        showStartTimePicker = false
                    }
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEndTimePicker) {
        val initialHour = formState.endTimeMinutes / 60
        val initialMinute = formState.endTimeMinutes % 60
        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            title = { Text("Select End Time", fontWeight = FontWeight.Bold) },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val minutes = timePickerState.hour * 60 + timePickerState.minute
                        onFieldChange { copy(endTimeMinutes = minutes) }
                        showEndTimePicker = false
                    }
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
