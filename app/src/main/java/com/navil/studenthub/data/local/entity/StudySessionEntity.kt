package com.navil.studenthub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampEpochMs: Long,
    val durationMinutes: Long,
    val sessionType: String = "Study", // "Study", "Short Break", "Long Break"
    val subject: String = ""
)
