package com.navil.studenthub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.navil.studenthub.model.AcademicDayOfWeek

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subject: String,
    val instructor: String = "",
    val dayOfWeek: AcademicDayOfWeek,
    val startTimeMinutes: Int, // Minutes from midnight (e.g., 9:30 AM = 570)
    val endTimeMinutes: Int,   // Minutes from midnight
    val room: String = "",
    val notes: String = "",
    val colorHex: String = "#2563EB"
)
