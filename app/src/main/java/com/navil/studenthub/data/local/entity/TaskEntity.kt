package com.navil.studenthub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.navil.studenthub.model.Priority

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val course: String = "",
    val description: String = "",
    val dueDateEpochDay: Long, // Epoch day
    val dueTimeMinutes: Int? = null, // Minutes from midnight (optional)
    val priority: Priority = Priority.MEDIUM,
    val hasReminder: Boolean = false,
    val isCompleted: Boolean = false,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
