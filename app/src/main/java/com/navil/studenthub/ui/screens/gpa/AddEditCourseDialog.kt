package com.navil.studenthub.ui.screens.gpa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.theme.AccentRose
import com.navil.studenthub.theme.PrimaryIndigo
import com.navil.studenthub.viewmodel.CourseFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCourseDialog(
    formState: CourseFormState,
    semesterName: String? = null,
    onFieldChange: (CourseFormState.() -> CourseFormState) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    var gradeDropdownExpanded by remember { mutableStateOf(false) }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (formState.id == null) "Add Course" else "Edit Course",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (semesterName != null) {
                            Text(
                                text = "Semester: $semesterName",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryIndigo,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "Set course credits and earned grade",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (formState.id != null && onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Course",
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

                // Course Name
                OutlinedTextField(
                    value = formState.courseName,
                    onValueChange = { newName -> onFieldChange { copy(courseName = newName) } },
                    label = { Text("Course Name / Code *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Credit Hours
                OutlinedTextField(
                    value = formState.creditHoursText,
                    onValueChange = { newCredits -> onFieldChange { copy(creditHoursText = newCredits) } },
                    label = { Text("Credit Hours (e.g. 3.0) *") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Grade Dropdown
                ExposedDropdownMenuBox(
                    expanded = gradeDropdownExpanded,
                    onExpandedChange = { gradeDropdownExpanded = !gradeDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = "${formState.grade.letter} (${String.format(java.util.Locale.ENGLISH, "%.2f", formState.grade.gradePoint)})",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Letter Grade *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = gradeDropdownExpanded,
                        onDismissRequest = { gradeDropdownExpanded = false }
                    ) {
                        GpaGrade.entries.forEach { g ->
                            DropdownMenuItem(
                                text = { Text("${g.letter}  —  ${String.format(java.util.Locale.ENGLISH, "%.2f", g.gradePoint)} pts") },
                                onClick = {
                                    onFieldChange { copy(grade = g) }
                                    gradeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

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
                        Text("Save Course", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
