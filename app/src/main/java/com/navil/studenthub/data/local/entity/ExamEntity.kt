package com.navil.studenthub.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exams",
    indices = [
        Index(value = ["courseId"]),
        Index(value = ["semesterId"]),
        Index(value = ["dateEpochDay"])
    ]
)
data class ExamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val courseId: Long? = null,
    val semesterId: Long? = null,
    val dateEpochDay: Long,
    val startTimeMinutes: Int,
    val endTimeMinutes: Int? = null,
    val room: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
