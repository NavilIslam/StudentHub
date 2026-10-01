package com.navil.studenthub.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.navil.studenthub.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Query("SELECT * FROM study_sessions ORDER BY timestampEpochMs DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE timestampEpochMs >= :startEpochMs ORDER BY timestampEpochMs DESC")
    fun getSessionsSince(startEpochMs: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE timestampEpochMs >= :startEpochMs")
    fun getTotalMinutesSince(startEpochMs: Long): Flow<Long?>

    @Query("SELECT COUNT(*) FROM study_sessions WHERE timestampEpochMs >= :startEpochMs")
    fun getSessionCountSince(startEpochMs: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Query("DELETE FROM study_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)
}
